package com.dcc.eventticketapp.data.Repository

import android.util.Log
import com.dcc.eventticketapp.data.Database.FavoriteDao
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Entities.FavoriteEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val favoriteDao: FavoriteDao,
    private val eventRepository: EventRepository
) {

    private fun currentUserId(): String {
        return auth.currentUser?.uid
            ?: throw Exception("Utilisateur non connecté")
    }

    /**
     * Récupère les events favoris complets (synchronise Firestore -> Room, puis lit Room).
     */
    // Dans FavoritesRepository.kt - MODIFIER getFavorites() :
    suspend fun getFavorites(): List<EventModel> {
        val userId = currentUserId()

        try {
            val snapshot = firestore
                .collection("users")
                .document(userId)
                .collection("favorites")
                .get()
                .await()

            val remoteIds = snapshot.documents.map { it.id }

            Log.d("FavoritesRepository", "Remote favorites = ${remoteIds.size}")

            // Insérer seulement les nouveaux (ou remplacer)
            favoriteDao.insertAll(
                remoteIds.map { eventId ->
                    FavoriteEntity(userId = userId, eventId = eventId)
                }
            )

        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Using cache", e)
        }

        val favoriteIds = favoriteDao.getFavoriteEventIds(userId)

        return favoriteIds.mapNotNull { eventId ->
            eventRepository.getEventById(eventId)
        }
    }

    suspend fun addFavorite(eventId: String) {
        val userId = currentUserId()

        firestore
            .collection("users")
            .document(userId)
            .collection("favorites")
            .document(eventId)
            .set(mapOf("addedAt" to System.currentTimeMillis()))
            .await()

        favoriteDao.insert(FavoriteEntity(userId = userId, eventId = eventId))
        Log.d("FavoritesRepository", "Added favorite: $eventId")
    }

    suspend fun removeFavorite(eventId: String) {
        val userId = currentUserId()

        firestore
            .collection("users")
            .document(userId)
            .collection("favorites")
            .document(eventId)
            .delete()
            .await()

        favoriteDao.delete(userId, eventId)
        Log.d("FavoritesRepository", "Removed favorite: $eventId")
    }

    // NOUVEAU : Toggle (ajouter si pas favori, supprimer si favori)
    suspend fun toggleFavorite(eventId: String): Boolean {
        val userId = currentUserId()
        val isCurrentlyFavorite = favoriteDao.isFavorite(userId, eventId)

        return if (isCurrentlyFavorite) {
            removeFavorite(eventId)
            false // N'est plus favori
        } else {
            addFavorite(eventId)
            true // Est maintenant favori
        }
    }

    suspend fun isFavorite(eventId: String): Boolean {
        val userId = currentUserId()
        return favoriteDao.isFavorite(userId, eventId)
    }

    suspend fun clearAllFavorites() {
        val userId = currentUserId()

        val snapshot = firestore
            .collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .await()

        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }

        favoriteDao.clearAll(userId)
    }

    suspend fun getFavoriteIds(): Set<String> {
        val userId = currentUserId()
        return favoriteDao.getFavoriteEventIds(userId).toSet()
    }
}