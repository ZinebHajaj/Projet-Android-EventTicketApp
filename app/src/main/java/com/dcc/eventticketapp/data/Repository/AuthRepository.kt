package com.dcc.eventticketapp.data.Repository

import com.dcc.eventticketapp.data.Entities.User
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        password: String
    ): User {

        val result = auth
            .createUserWithEmailAndPassword(email, password)
            .await()

        val firebaseUser = result.user
            ?: throw Exception("User creation failed")

        val user = User(
            userId = firebaseUser.uid,
            fullName = fullName,
            email = email,
            phone = phone
        )

        android.util.Log.d("FIRESTORE_TEST", "Before Save")

        firestore
            .collection("users")
            .document(firebaseUser.uid)
            .set(user)
            .await()

        android.util.Log.d("FIRESTORE_TEST", "After Save")

        return user
    }

    suspend fun login(
        email: String,
        password: String
    ): User {

        val result = auth
            .signInWithEmailAndPassword(email, password)
            .await()

        val firebaseUser = result.user
            ?: throw Exception("Login failed")

        val snapshot = firestore
            .collection("users")
            .document(firebaseUser.uid)
            .get()
            .await()

        return snapshot.toObject(User::class.java)
            ?: throw Exception("User profile not found")
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun currentUserId(): String? {
        return auth.currentUser?.uid
    }

    suspend fun getCurrentUser(): User? {

        val uid = auth.currentUser?.uid
            ?: return null

        val snapshot = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(User::class.java)
    }

    suspend fun updateProfile(
        fullName: String,
        phone: String
    ): User {

        val firebaseUser =
            auth.currentUser
                ?: throw Exception("Utilisateur non connecté")

        firestore
            .collection("users")
            .document(firebaseUser.uid)
            .update(
                mapOf(
                    "fullName" to fullName,
                    "phone" to phone
                )
            )
            .await()

        return getCurrentUser()
            ?: throw Exception("Erreur chargement profil")
    }


    suspend fun signInWithGoogle(idToken: String): User {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        val result = auth
            .signInWithCredential(credential)
            .await()

        val firebaseUser = result.user
            ?: throw Exception("Google sign-in failed")

        // si l'utilisateur existe déjà dans Firestore
        val snapshot = firestore
            .collection("users")
            .document(firebaseUser.uid)
            .get()
            .await()

        // S'il n'existe pas -> créer son profil
        if (!snapshot.exists()) {
            val user = User(
                userId   = firebaseUser.uid,
                fullName = firebaseUser.displayName ?: "",
                email    = firebaseUser.email ?: "",
                phone    = ""
            )
            firestore
                .collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()
        }

        return snapshot.toObject(User::class.java)
            ?: User(
                userId   = firebaseUser.uid,
                fullName = firebaseUser.displayName ?: "",
                email    = firebaseUser.email ?: ""
            )
    }


    suspend fun signInWithFacebook(accessToken: AccessToken): User {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        val result = auth.signInWithCredential(credential).await()
        val firebaseUser = result.user ?: throw Exception("Facebook sign-in failed")

        val snapshot = firestore
            .collection("users")
            .document(firebaseUser.uid)
            .get()
            .await()

        if (!snapshot.exists()) {
            val user = User(
                userId   = firebaseUser.uid,
                fullName = firebaseUser.displayName ?: "",
                email    = firebaseUser.email ?: "",
                phone    = ""
            )
            firestore.collection("users").document(firebaseUser.uid).set(user).await()
        }

        return snapshot.toObject(User::class.java)
            ?: User(
                userId   = firebaseUser.uid,
                fullName = firebaseUser.displayName ?: "",
                email    = firebaseUser.email ?: ""
            )
    }

}