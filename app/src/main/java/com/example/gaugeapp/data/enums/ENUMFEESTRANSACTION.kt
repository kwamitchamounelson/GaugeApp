package com.example.gaugeapp.data.enums

import com.example.gaugeapp.data.entities.FeesTransaction
import com.kola.smsmodule.enums.ENUM_SERVICE_ANALYSIS

enum class ENUMFEESTRANSACTION(val feesTransaction: FeesTransaction) {
    //ORANGE
    //cas des retraits
    OM_WITHDRAW_50_TO_6500(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_50_TO_6500",
            50.0,
            6500.0,
            0.03,
            0.0
        )
    ),
    OM_WITHDRAW_6501_TO_10000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_6501_TO_10000",
            6501.0,
            10000.0,
            0.0,
            180.0
        )
    ),
    OM_WITHDRAW_10001_TO_13500(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_10001_TO_13500",
            10001.0,
            13500.0,
            0.0,
            300.0
        )
    ),
    OM_WITHDRAW_13501_TO_25000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_13501_TO_25000",
            13501.0,
            25000.0,
            0.0,
            350.0
        )
    ),
    OM_WITHDRAW_25001_TO_50000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_25001_TO_50000",
            25001.0,
            50000.0,
            0.0,
            700.0
        )
    ),
    OM_WITHDRAW_50001_TO_80000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_50001_TO_80000",
            50001.0,
            80000.0,
            0.0,
            1350.0
        )
    ),
    OM_WITHDRAW_80001_TO_100000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_80001_TO_100000",
            80001.0,
            100000.0,
            0.0,
            1800.0
        )
    ),
    OM_WITHDRAW_100001_TO_200000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_100001_TO_200000",
            100001.0,
            200000.0,
            0.0,
            2150.0
        )
    ),
    OM_WITHDRAW_200001_TO_300000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_200001_TO_300000",
            200001.0,
            300000.0,
            0.0,
            2600.0
        )
    ),
    OM_WITHDRAW_300001_TO_400000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_300001_TO_400000",
            300001.0,
            400000.0,
            0.0,
            3100.0
        )
    ),
    OM_WITHDRAW_400001_TO_500000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_ORANGE,
            "OM_WITHDRAW_400001_TO_500000",
            400001.0,
            500000.0,
            0.0,
            3600.0
        )
    ),


    //cas des transfer
    OM_TRANSFER_50_TO_6500(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_50_TO_6500",
            50.0,
            6500.0,
            0.01,
            0.0
        )
    ),
    OM_TRANSFER_6501_TO_10000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_6501_TO_10000",
            6501.0,
            10000.0,
            0.0,
            50.0
        )
    ),
    OM_TRANSFER_10001_TO_13500(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_10001_TO_13500",
            10001.0,
            13500.0,
            0.0,
            100.0
        )
    ),
    OM_TRANSFER_13501_TO_25000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_13501_TO_25000",
            13501.0,
            25000.0,
            0.0,
            150.0
        )
    ),
    OM_TRANSFER_25001_TO_50000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_25001_TO_50000",
            25001.0,
            50000.0,
            0.0,
            150.0
        )
    ),
    OM_TRANSFER_50001_TO_80000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_50001_TO_80000",
            50001.0,
            80000.0,
            0.0,
            200.0
        )
    ),
    OM_TRANSFER_80001_TO_100000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_80001_TO_100000",
            80001.0,
            100000.0,
            0.0,
            200.0
        )
    ),
    OM_TRANSFER_100001_TO_200000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_100001_TO_200000",
            100001.0,
            200000.0,
            0.0,
            300.0
        )
    ),
    OM_TRANSFER_200001_TO_300000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_200001_TO_300000",
            200001.0,
            300000.0,
            0.0,
            300.0
        )
    ),
    OM_TRANSFER_300001_TO_400000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_300001_TO_400000",
            300001.0,
            400000.0,
            0.0,
            300.0
        )
    ),
    OM_TRANSFER_400001_TO_500000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_400001_TO_500000",
            400001.0,
            500000.0,
            0.0,
            500.0
        )
    ),
    OM_TRANSFER_500001_TO_1000000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_ORANGE,
            "OM_TRANSFER_400001_TO_500000",
            500001.0,
            1000000.0,
            0.0,
            500.0
        )
    ),


    //MTN
    //cas des transferts
    MOMO_TRANSFER_100_TO_10050(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_MTN,
            "MOMO_TRANSFER_100_TO_10050",
            100.0,
            10050.0,
            0.01,
            0.0
        )
    ),
    MOMO_TRANSFER_10051_TO_13550(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_MTN,
            "MOMO_TRANSFER_10051_TO_13550",
            10051.0,
            13550.0,
            0.0,
            100.0
        )
    ),
    MOMO_TRANSFER_13551_TO_1000000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.TRANSFERT_MTN,
            "MOMO_TRANSFER_13551_TO_1000000",
            13551.0,
            1000000.0,
            0.0,
            125.0
        )
    ),

    //cas des retrait
    MOMO_WITHDRAW_100_TO_5999(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_100_TO_5999",
            100.0,
            5999.0,
            0.03,
            0.0
        )
    ),
    MOMO_WITHDRAW_6000_TO_10050(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_6000_TO_10050",
            6000.0,
            10050.0,
            0.0,
            175.0
        )
    ),
    MOMO_WITHDRAW_10051_TO_13550(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_10051_TO_13550",
            10051.0,
            13550.0,
            0.0,
            300.0
        )
    ),
    MOMO_WITHDRAW_13551_TO_25050(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_13551_TO_25050",
            13551.0,
            25050.0,
            0.0,
            350.0
        )
    ),
    MOMO_WITHDRAW_25051_TO_50050(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_25051_TO_50050",
            25051.0,
            50050.0,
            0.0,
            700.0
        )
    ),
    MOMO_WITHDRAW_50051_TO_75100(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_50051_TO_75100",
            50051.0,
            75100.0,
            0.0,
            1350.0
        )
    ),
    MOMO_WITHDRAW_75101_TO_100100(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_75101_TO_100100",
            75101.0,
            100100.0,
            0.0,
            1800.0
        )
    ),
    MOMO_WITHDRAW_100101_TO_200500(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_100101_TO_200500",
            100101.0,
            200500.0,
            0.0,
            2150.0
        )
    ),
    MOMO_WITHDRAW_200501_TO_300500(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_200501_TO_300500",
            200501.0,
            300500.0,
            0.0,
            2600.0
        )
    ),
    MOMO_WITHDRAW_300501_TO_400500(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_300501_TO_400500",
            300501.0,
            400500.0,
            0.0,
            3100.0
        )
    ),
    MOMO_WITHDRAW_400501_TO_500000(
        FeesTransaction(
            ENUM_SERVICE_ANALYSIS.RETRAIT_MTN,
            "MOMO_WITHDRAW_400501_TO_500000",
            400501.0,
            500000.0,
            0.0,
            3500.0
        )
    )
}