package com.example.gaugeapp.repositories.creditRepositories

import com.example.gaugeapp.entities.Store
import com.example.gaugeapp.utils.DataState
import com.kola.kola_entities_features.entities.Localization
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class StoreRepository @Inject constructor() {
    fun getAllStores(): Flow<DataState<List<Store>>> {
        //mock data
        return flow {
            val list = listOf<Store>(
                Store(
                    "1",
                    "Casino",
                    "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/STORES%2Fcasino.jpg?alt=media&token=896dfcb3-0ae7-4b85-acba-88bfb70e997f",
                    "690935868",
                    "678243790",
                    "Yaounde",
                    Localization(11.520612738784084, 3.8628988555103922, 0.0, ""),
                    Date(),
                    Date()
                ),
                Store(
                    "2",
                    "Dovv Bastos",
                    "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/STORES%2Fdov.jpg?alt=media&token=1b76fc54-97c9-4c09-8b41-c45992b58a1a",
                    "690935868",
                    "678243790",
                    "Yaounde",
                    Localization(11.510128309519496, 3.8928423625063187, 0.0, ""),
                    Date(),
                    Date()
                ),
                Store(
                    "3",
                    "NIKI MOKOLO",
                    "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/STORES%2Fniki.png?alt=media&token=6d6d0f8d-3538-48f6-a906-2bddc44ab16f",
                    "690935868",
                    "678243790",
                    "Yaounde",
                    Localization(11.499424438354607, 3.8746708791512727, 0.0, ""),
                    Date(),
                    Date()
                ),
                Store(
                    "4",
                    "Quifeurou-Mvog-Ada",
                    "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/STORES%2Fquiferou.jpg?alt=media&token=8ae2f990-9aba-425e-b6e6-820d9e9bad86",
                    "690935868",
                    "678243790",
                    "Yaounde",
                    Localization(11.531534424859878, 3.8686212981758628, 0.0, ""),
                    Date(),
                    Date()
                ),
                Store(
                    "5",
                    "Santa Lucia Mvan",
                    "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/STORES%2Fsanta%20lucia.jpg?alt=media&token=9d81b863-6d8c-4ecd-bf5b-e4119f9e5212",
                    "690935868",
                    "678243790",
                    "Yaounde",
                    Localization(11.517167511365118, 3.825485510671145, 0.0, ""),
                    Date(),
                    Date()
                ),
                Store(
                    "5",
                    "Mahima Coron",
                    "https://firebasestorage.googleapis.com/v0/b/kola-wallet-dev.appspot.com/o/STORES%2Fmahima.jpg?alt=media&token=65e1db30-e186-4bd7-b8ff-0831a23dc9d2",
                    "690935868",
                    "678243790",
                    "Yaounde",
                    Localization(11.519086253694978, 3.8464813373063027, 0.0, ""),
                    Date(),
                    Date()
                )
            )

            emit(DataState.Success(list))
        }
    }
}