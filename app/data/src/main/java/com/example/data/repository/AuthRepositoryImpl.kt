package com.example.data.repository

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await // Added import for .await()
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth, // Injected FirebaseAuth
    private val firestore: FirebaseFirestore // Injected FirebaseFirestore
) : AuthRepository {


    override fun getCurrentUser(): User? {
        return auth.currentUser
    //return firebaseAuth.currentUser?.toDomainUser()
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            firebaseUser?.let { Result.success(it.toDomainUser()) }
                ?: Result.failure(Exception("User not found"))
        } catch (e: Exception) {
            Result.failure(e)

        }
    }

    override suspend fun registerUser(email: String, password: String): Result<User> {
        return try {
            val userCredential = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = userCredential.user
            if (firebaseUser != null) {
                // Create a user document in Firestore
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    role = "user",
                    status = "pending"
                )
                firestore.collection("users").document(firebaseUser.uid).set(user).await()
                Result.Success(Unit)
            } else {
                Result.Error("User registration failed: user object is null.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown registration error occurred.")
        }
    }

    //registerUser should call createUserWithEmailAndPassword on the firebaseAuth instance
    // upon successful auth creation, it must also create a new user document in the database with uid, email, role of "user" and status of "pending"

    override suspend fun signup(name: String, email: String, password: String): Result<User> {
        // Similar logic for signup, map FirebaseUser to domain User upon success
        return try {
            val firebaseUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            // You might want to update the FirebaseUser's display name here as well
            // For example:
            // if (firebaseUser != null) {
            //     val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            //         .setDisplayName(name)
            //         .build()
            //     firebaseUser.updateProfile(profileUpdates).await()
            // }
            firebaseUser?.let { Result.success(it.toDomainUser()) }
                ?: Result.failure(Exception("Signup failed: Could not create user"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    //Add GetPendingUsers function here

    //Add ApprovePendingUser function here
    // Screen should display a list of users where status == "pending"
    // Each user item should have an Approve button and a Delete button that trigger the respective use cases

    //Add DeleteUser function here

}

// Helper extension function to map FirebaseUser to your domain User
// This can be in the same file or a separate mapper file in the :data layer
// !!! IMPORTANT: Adjust this function to match the properties of your domain.model.User !!!
fun FirebaseUser.toDomainUser(): User {
    return User(
        uid = "String",
        email = "String",
         role = "user", // Only include if your domain.model.User has 'role'
         status = "pending" // Only include if your domain.model.User has 'status'
    )
}


//Was here before changes
//    override fun getCurrentUser: FirebaseUser? {
//        return auth.currentUser
//    }
