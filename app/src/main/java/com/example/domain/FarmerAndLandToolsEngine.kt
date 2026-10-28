package com.example.domain

import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class LandShareItem(
    val shareIndex: Int,
    val holderName: String,
    val sharePercent: Double,
    val areaSqFt: Double,
    val areaSqMeters: Double,
    val primaryRegionalUnitValue: Double,
    val gunthaValue: Double,
    val vaarValue: Double,
    val frontageWidthFt: Double,
    val depthFt: Double,
    val estimatedValueInr: Double
)

data class LandPartitionResult(
    val grossAreaSqFt: Double,
    val roadOrCanalCutSqFt: Double,
    val netDistributableSqFt: Double,
    val totalFrontageFt: Double,
    val plotDepthFt: Double,
    val primaryUnitName: String,
    val shares: List<LandShareItem>
)

enum class FarmerCropSpec(
    val id: String,
    val cropName: String,
    val gujaratiName: String,
    val seedKgPerAcre: Double,
    val dapBagsPerAcre: Double,
    val ureaBagsPerAcre: Double,
    val potashBagsPerAcre: Double,
    val tractorHoursPerAcre: Double,
    val irrigationLitersPerAcrePerCycle: Double,
    val expectedYieldManPerAcre: Double, // 1 Man (મણ) = 20 Kg
    val avgMarketPricePerManInr: Double
) {
    COTTON(
        id = "cotton",
        cropName = "Cotton (BT Hybrid)",
        gujaratiName = "કપાસ (BT Cotton)",
        seedKgPerAcre = 0.9,
        dapBagsPerAcre = 1.2,
        ureaBagsPerAcre = 2.5,
        potashBagsPerAcre = 0.8,
        tractorHoursPerAcre = 2.5,
        irrigationLitersPerAcrePerCycle = 140000.0,
        expectedYieldManPerAcre = 55.0,
        avgMarketPricePerManInr = 1550.0
    ),
    GROUNDNUT(
        id = "groundnut",
        cropName = "Groundnut (Peanut)",
        gujaratiName = "મગફળી (Groundnut)",
        seedKgPerAcre = 48.0,
        dapBagsPerAcre = 1.0,
        ureaBagsPerAcre = 0.6,
        potashBagsPerAcre = 0.8,
        tractorHoursPerAcre = 3.0,
        irrigationLitersPerAcrePerCycle = 120000.0,
        expectedYieldManPerAcre = 48.0,
        avgMarketPricePerManInr = 1350.0
    ),
    WHEAT(
        id = "wheat",
        cropName = "Wheat (Irrigated)",
        gujaratiName = "ઘઉં (Wheat)",
        seedKgPerAcre = 45.0,
        dapBagsPerAcre = 1.1,
        ureaBagsPerAcre = 2.2,
        potashBagsPerAcre = 0.5,
        tractorHoursPerAcre = 2.2,
        irrigationLitersPerAcrePerCycle = 150000.0,
        expectedYieldManPerAcre = 90.0,
        avgMarketPricePerManInr = 560.0
    ),
    CUMIN(
        id = "cumin",
        cropName = "Cumin (Jeera)",
        gujaratiName = "જીરું (Cumin Spice)",
        seedKgPerAcre = 5.5,
        dapBagsPerAcre = 0.8,
        ureaBagsPerAcre = 1.0,
        potashBagsPerAcre = 0.4,
        tractorHoursPerAcre = 2.0,
        irrigationLitersPerAcrePerCycle = 95000.0,
        expectedYieldManPerAcre = 18.0,
        avgMarketPricePerManInr = 4800.0
    ),
    PADDY(
        id = "paddy",
        cropName = "Paddy / Rice",
        gujaratiName = "ડાંગર (Paddy)",
        seedKgPerAcre = 14.0,
        dapBagsPerAcre = 1.3,
        ureaBagsPerAcre = 2.6,
        potashBagsPerAcre = 0.9,
        tractorHoursPerAcre = 3.5,
        irrigationLitersPerAcrePerCycle = 240000.0,
        expectedYieldManPerAcre = 110.0,
        avgMarketPricePerManInr = 480.0
    ),
    ONION(
        id = "onion",
        cropName = "Onion / Garlic",
        gujaratiName = "ડુંગળી / લસણ (Onion)",
        seedKgPerAcre = 4.0,
        dapBagsPerAcre = 1.5,
        ureaBagsPerAcre = 2.0,
        potashBagsPerAcre = 1.2,
        tractorHoursPerAcre = 2.8,
        irrigationLitersPerAcrePerCycle = 130000.0,
        expectedYieldManPerAcre = 380.0,
        avgMarketPricePerManInr = 320.0
    ),
    SUGARCANE(
        id = "sugarcane",
        cropName = "Sugarcane",
        gujaratiName = "શેરડી (Sugarcane)",
        seedKgPerAcre = 2400.0,
        dapBagsPerAcre = 2.2,
        ureaBagsPerAcre = 4.5,
        potashBagsPerAcre = 1.8,
        tractorHoursPerAcre = 4.5,
        irrigationLitersPerAcrePerCycle = 280000.0,
        expectedYieldManPerAcre = 1800.0,
        avgMarketPricePerManInr = 72.0
    ),
    CASTOR(
        id = "castor",
        cropName = "Castor (Divela)",
        gujaratiName = "એરંડા (Castor)",
        seedKgPerAcre = 2.5,
        dapBagsPerAcre = 1.0,
        ureaBagsPerAcre = 1.8,
        potashBagsPerAcre = 0.5,
        tractorHoursPerAcre = 2.0,
        irrigationLitersPerAcrePerCycle = 125000.0,
        expectedYieldManPerAcre = 52.0,
        avgMarketPricePerManInr = 1220.0
    )
}

data class FarmerCropEstimateResult(
    val crop: FarmerCropSpec,
    val areaSqFt: Double,
    val areaAcres: Double,
    val areaVigha: Double,
    val totalSeedKg: Double,
    val dapBags50Kg: Double,
    val ureaBags45Kg: Double,
    val potashBags50Kg: Double,
    val tractorHours: Double,
    val tractorDieselLiters: Double,
    val irrigationWaterLitersPerCycle: Double,
    val expectedYieldMan20Kg: Double,
    val expectedYieldQuintal: Double,
    val estimatedInputCostInr: Double,
    val estimatedGrossIncomeInr: Double,
    val estimatedNetProfitInr: Double
)

data class FarmFencingEstimateResult(
    val perimeterFeet: Double,
    val perimeterMeters: Double,
    val poleSpacingFeet: Double,
    val barbedWireRows: Int,
    val mainPolesCount: Int,
    val cornerStrutPolesCount: Int,
    val totalPolesCount: Int,
    val totalWireLengthMeters: Double,
    val totalBarbedWireWeightKg: Double,
    val cementBagsForPoles: Int,
    val totalEstimatedFencingCostInr: Double
)

data class Revenue712DecodeResult(
    val hectares: Double,
    val ares: Double,
    val sqMeters: Double,
    val totalSqMeters: Double,
    val totalSqFeet: Double,
    val saurashtraVigha16Guntha: Double,
    val northGujaratVigha23_5Guntha: Double,
    val centralGujaratKachhaVigha: Double,
    val totalGuntha: Double,
    val totalVaarSqYd: Double,
    val totalAcres: Double,
    val kccCropLoanEligibilityInr: Double
)

data class WaterTankAndBorewellResult(
    val isCircularWell: Boolean,
    val volumeCubicFeet: Double,
    val volumeCubicMeters: Double,
    val totalCapacityLiters: Double,
    val irrigationHoursAt250Lpm: Double,
    val borewellDepthFeet: Double,
    val recommendedSubmersibleHp: Double,
    val powerConsumptionKwhPerHour: Double
)

data class DailyMandiBillResult(
    val totalBags: Int,
    val avgBagWeightKg: Double,
    val bardaanWeightPerBagKg: Double,
    val grossWeightKg: Double,
    val totalBardaanDeductionKg: Double,
    val netWeightKg: Double,
    val netWeightMan20Kg: Double,
    val netWeightQuintal: Double,
    val netWeightKhandi: Double,
    val ratePer20KgManInr: Double,
    val equivalentRatePerQuintalInr: Double,
    val ratePerKgInr: Double,
    val grossAmountInr: Double,
    val commissionAmountInr: Double,
    val hamaliAndTolaiInr: Double,
    val netPayableToFarmerInr: Double
)

data class DailyLabourTractorKhataResult(
    val workersCount: Int,
    val daysWorked: Double,
    val totalPersonDays: Double,
    val dailyWagePerWorkerInr: Double,
    val totalLabourWageInr: Double,
    val tractorHours: Double,
    val tractorRatePerHourInr: Double,
    val totalTractorRentInr: Double,
    val estimatedDieselLiters: Double,
    val grossCombinedBillInr: Double,
    val upaadAdvanceGivenInr: Double,
    val netBalancePayableInr: Double
)

data class LandDealBrokerageResult(
    val totalDealAmountInr: Double,
    val tokenAmountInr: Double,
    val banakhatPercent: Double,
    val banakhatAmountInr: Double,
    val remainingOnDastavejInr: Double,
    val buyerBrokeragePercent: Double,
    val sellerBrokeragePercent: Double,
    val buyerBrokerageInr: Double,
    val sellerBrokerageInr: Double,
    val totalBrokeragePoolInr: Double,
    val totalBuyerOutflowInr: Double,
    val netSellerHandoverInr: Double
)

data class DailyFarmDairyRojmelResult(
    val dailyMilkLiters: Double,
    val milkRatePerLiterInr: Double,
    val dailyMilkIncomeInr: Double,
    val otherDailyFarmIncomeInr: Double,
    val totalDailyIncomeInr: Double,
    val cattleFeedAndCharaExpenseInr: Double,
    val dieselAndFertilizerExpenseInr: Double,
    val miscDailyExpenseInr: Double,
    val totalDailyExpenseInr: Double,
    val netDailyProfitInr: Double,
    val projectedMonthlyNetProfitInr: Double
)

object FarmerAndLandToolsEngine {

    /**
     * Splits a land parcel among N family heirs/partners after deducting road/canal cut.
     */
    fun calculateLandPartition(
        grossAreaSqFt: Double,
        roadCutPercent: Double,
        numberOfHeirs: Int,
        stateProfile: StateLandProfile,
        ratePerSqFtInr: Double = 1200.0
    ): LandPartitionResult {
        val safeGross = max(100.0, grossAreaSqFt)
        val safeCutPct = roadCutPercent.coerceIn(0.0, 60.0)
        val cutSqFt = safeGross * (safeCutPct / 100.0)
        val netSqFt = max(1.0, safeGross - cutSqFt)
        val heirsCount = numberOfHeirs.coerceIn(1, 12)

        val depthFt = sqrt(netSqFt / 1.25)
        val totalFrontageFt = netSqFt / max(1.0, depthFt)

        val sharePct = 100.0 / heirsCount
        val shareAreaSqFt = netSqFt / heirsCount
        val shareFrontageFt = totalFrontageFt / heirsCount
        val primaryUnitSqFt = max(1.0, stateProfile.vighaSqFt)
        val primaryUnitName = stateProfile.units.firstOrNull()?.name?.substringBefore("(")?.trim() ?: "Vigha"

        val shares = (1..heirsCount).map { idx ->
            LandShareItem(
                shareIndex = idx,
                holderName = "Heir / Share #$idx (ભાગ-$idx)",
                sharePercent = sharePct,
                areaSqFt = shareAreaSqFt,
                areaSqMeters = shareAreaSqFt * 0.09290304,
                primaryRegionalUnitValue = shareAreaSqFt / primaryUnitSqFt,
                gunthaValue = shareAreaSqFt / 1089.0,
                vaarValue = shareAreaSqFt / 9.0,
                frontageWidthFt = shareFrontageFt,
                depthFt = depthFt,
                estimatedValueInr = shareAreaSqFt * ratePerSqFtInr
            )
        }

        return LandPartitionResult(
            grossAreaSqFt = safeGross,
            roadOrCanalCutSqFt = cutSqFt,
            netDistributableSqFt = netSqFt,
            totalFrontageFt = totalFrontageFt,
            plotDepthFt = depthFt,
            primaryUnitName = primaryUnitName,
            shares = shares
        )
    }

    /**
     * Calculates seed, fertilizer bags, tractor hours, irrigation water, and expected crop yield in Man (20 Kg) & Quintal.
     */
    fun calculateFarmerCropInputs(
        crop: FarmerCropSpec,
        areaSqFt: Double,
        vighaSqFt: Double
    ): FarmerCropEstimateResult {
        val safeAreaSqFt = max(100.0, areaSqFt)
        val acres = safeAreaSqFt / 43560.0
        val vighas = safeAreaSqFt / max(1.0, vighaSqFt)

        val seedKg = acres * crop.seedKgPerAcre
        val dapBags = acres * crop.dapBagsPerAcre
        val ureaBags = acres * crop.ureaBagsPerAcre
        val potashBags = acres * crop.potashBagsPerAcre
        val tractorHrs = acres * crop.tractorHoursPerAcre
        val dieselLiters = tractorHrs * 4.2
        val waterLiters = acres * crop.irrigationLitersPerAcrePerCycle

        val yieldMan = acres * crop.expectedYieldManPerAcre
        val yieldQuintal = (yieldMan * 20.0) / 100.0

        val seedCost = seedKg * 320.0
        val fertCost = (dapBags * 1350.0) + (ureaBags * 270.0) + (potashBags * 1650.0)
        val tractorCost = tractorHrs * 900.0
        val totalInputCost = seedCost + fertCost + tractorCost
        val grossIncome = yieldMan * crop.avgMarketPricePerManInr
        val netProfit = grossIncome - totalInputCost

        return FarmerCropEstimateResult(
            crop = crop,
            areaSqFt = safeAreaSqFt,
            areaAcres = acres,
            areaVigha = vighas,
            totalSeedKg = seedKg,
            dapBags50Kg = dapBags,
            ureaBags45Kg = ureaBags,
            potashBags50Kg = potashBags,
            tractorHours = tractorHrs,
            tractorDieselLiters = dieselLiters,
            irrigationWaterLitersPerCycle = waterLiters,
            expectedYieldMan20Kg = yieldMan,
            expectedYieldQuintal = yieldQuintal,
            estimatedInputCostInr = totalInputCost,
            estimatedGrossIncomeInr = grossIncome,
            estimatedNetProfitInr = netProfit
        )
    }

    /**
     * Calculates boundary fencing poles, barbed wire rows, weight in Kg, and total cost.
     */
    fun calculateFarmFencing(
        perimeterFeet: Double,
        poleSpacingFeet: Double = 8.0,
        barbedWireRows: Int = 5,
        poleRateInr: Double = 420.0,
        wireRatePerKgInr: Double = 95.0
    ): FarmFencingEstimateResult {
        val safePerimFt = max(20.0, perimeterFeet)
        val perimMeters = safePerimFt / 3.280839895
        val safeSpacingFt = poleSpacingFeet.coerceIn(4.0, 20.0)
        val rows = barbedWireRows.coerceIn(2, 10)

        val mainPoles = ceil(safePerimFt / safeSpacingFt).toInt()
        val cornerSupportPoles = 8
        val totalPoles = mainPoles + cornerSupportPoles

        val totalWireMeters = perimMeters * rows * 1.05
        val totalWireKg = totalWireMeters / 7.5
        val cementBags = ceil(totalPoles / 6.0).roundToInt().coerceAtLeast(1)

        val totalCost = (totalPoles * poleRateInr) +
            (totalWireKg * wireRatePerKgInr) +
            (cementBags * 390.0)

        return FarmFencingEstimateResult(
            perimeterFeet = safePerimFt,
            perimeterMeters = perimMeters,
            poleSpacingFeet = safeSpacingFt,
            barbedWireRows = rows,
            mainPolesCount = mainPoles,
            cornerStrutPolesCount = cornerSupportPoles,
            totalPolesCount = totalPoles,
            totalWireLengthMeters = totalWireMeters,
            totalBarbedWireWeightKg = totalWireKg,
            cementBagsForPoles = cementBags,
            totalEstimatedFencingCostInr = totalCost
        )
    }

    /**
     * Decodes Government 7/12 & 8A Revenue Extract format (Hectare - Are - Sq.Meter) into
     * Saurashtra Vigha, North Gujarat Vigha, Guntha, Vaar, Acre, Sq.Ft, and KCC Crop Loan estimate.
     */
    fun calculate712HectareAreSqm(
        hectares: Double,
        ares: Double,
        sqMeters: Double
    ): Revenue712DecodeResult {
        val h = max(0.0, hectares)
        val a = max(0.0, ares)
        val m = max(0.0, sqMeters)
        val totalSqM = (h * 10000.0) + (a * 100.0) + m
        val totalSqFt = totalSqM * 10.7639104167

        val saurashtraVigha = totalSqFt / 17424.0
        val northGujVigha = totalSqFt / 25591.5
        val kachhaVigha = totalSqFt / 14288.4
        val guntha = totalSqFt / 1089.0
        val vaar = totalSqFt / 9.0
        val acres = totalSqFt / 43560.0

        // Standard Kisan Credit Card (KCC) irrigated crop scale of finance ~ ₹68,000 per Acre
        val kccLoanInr = acres * 68000.0

        return Revenue712DecodeResult(
            hectares = h,
            ares = a,
            sqMeters = m,
            totalSqMeters = totalSqM,
            totalSqFeet = totalSqFt,
            saurashtraVigha16Guntha = saurashtraVigha,
            northGujaratVigha23_5Guntha = northGujVigha,
            centralGujaratKachhaVigha = kachhaVigha,
            totalGuntha = guntha,
            totalVaarSqYd = vaar,
            totalAcres = acres,
            kccCropLoanEligibilityInr = kccLoanInr
        )
    }

    /**
     * Calculates Farm Water Tank / Pond / Circular Well storage capacity in Liters
     * and recommended Borewell Submersible Pump HP.
     */
    fun calculateWaterTankAndBorewellPump(
        isCircularWell: Boolean,
        lengthOrDiameterFt: Double,
        widthFt: Double,
        depthFt: Double,
        borewellDepthFt: Double,
        dischargeLpm: Double = 250.0
    ): WaterTankAndBorewellResult {
        val safeL = max(1.0, lengthOrDiameterFt)
        val safeW = max(1.0, widthFt)
        val safeD = max(1.0, depthFt)

        val volumeCuFt = if (isCircularWell) {
            val radius = safeL / 2.0
            PI * radius * radius * safeD
        } else {
            safeL * safeW * safeD
        }
        val volumeCuM = volumeCuFt * 0.0283168466
        val capacityLiters = volumeCuFt * 28.3168466
        val safeDischarge = max(20.0, dischargeLpm)
        val irrigationHrs = (capacityLiters / safeDischarge) / 60.0

        // Hydraulic Pump Power Formula: HP = (Head_Meters * Flow_LPS) / (75 * PumpEfficiency_0.55)
        val headMeters = max(10.0, borewellDepthFt) * 0.3048
        val flowLps = safeDischarge / 60.0
        val rawHp = (headMeters * flowLps) / (75.0 * 0.55)
        val standardHp = when {
            rawHp <= 1.5 -> 1.5
            rawHp <= 3.0 -> 3.0
            rawHp <= 5.0 -> 5.0
            rawHp <= 7.5 -> 7.5
            rawHp <= 10.0 -> 10.0
            rawHp <= 12.5 -> 12.5
            rawHp <= 15.0 -> 15.0
            else -> 20.0
        }
        val kwhPerHour = standardHp * 0.746

        return WaterTankAndBorewellResult(
            isCircularWell = isCircularWell,
            volumeCubicFeet = volumeCuFt,
            volumeCubicMeters = volumeCuM,
            totalCapacityLiters = capacityLiters,
            irrigationHoursAt250Lpm = irrigationHrs,
            borewellDepthFeet = max(10.0, borewellDepthFt),
            recommendedSubmersibleHp = standardHp,
            powerConsumptionKwhPerHour = kwhPerHour
        )
    }

    /**
     * Daily APMC Mandi Weight (20 kg Man / 100 kg Quintal / Khandi), Bardaan tare deduction & Net Crop Bill.
     */
    fun calculateDailyMandiBill(
        totalBags: Int,
        avgBagWeightKg: Double,
        bardaanWeightPerBagKg: Double,
        ratePer20KgManInr: Double,
        commissionPercent: Double = 1.0,
        hamaliPerBagInr: Double = 8.0
    ): DailyMandiBillResult {
        val bags = max(1, totalBags)
        val bagWt = max(1.0, avgBagWeightKg)
        val tareWt = bardaanWeightPerBagKg.coerceIn(0.0, 5.0)
        val grossKg = bags * bagWt
        val totalTareKg = bags * tareWt
        val netKg = max(0.0, grossKg - totalTareKg)

        val netMan20Kg = netKg / 20.0
        val netQuintal100Kg = netKg / 100.0
        val netKhandi = netMan20Kg / 20.0 // 1 Khandi = 20 Man = 400 Kg

        val safeRateMan = max(0.0, ratePer20KgManInr)
        val rateQuintal = safeRateMan * 5.0
        val rateKg = safeRateMan / 20.0

        val grossAmount = netMan20Kg * safeRateMan
        val commAmount = grossAmount * (commissionPercent.coerceIn(0.0, 10.0) / 100.0)
        val hamaliAmount = bags * max(0.0, hamaliPerBagInr)
        val netPayable = max(0.0, grossAmount - commAmount - hamaliAmount)

        return DailyMandiBillResult(
            totalBags = bags,
            avgBagWeightKg = bagWt,
            bardaanWeightPerBagKg = tareWt,
            grossWeightKg = grossKg,
            totalBardaanDeductionKg = totalTareKg,
            netWeightKg = netKg,
            netWeightMan20Kg = netMan20Kg,
            netWeightQuintal = netQuintal100Kg,
            netWeightKhandi = netKhandi,
            ratePer20KgManInr = safeRateMan,
            equivalentRatePerQuintalInr = rateQuintal,
            ratePerKgInr = rateKg,
            grossAmountInr = grossAmount,
            commissionAmountInr = commAmount,
            hamaliAndTolaiInr = hamaliAmount,
            netPayableToFarmerInr = netPayable
        )
    }

    /**
     * Daily Farm Labour (Majur / Dadiya), Tractor Hours & Upaad (Advance) Khata Calculator.
     */
    fun calculateDailyLabourAndTractorKhata(
        maleOrGeneralWorkers: Int,
        daysWorked: Double,
        dailyWagePerWorkerInr: Double,
        tractorHours: Double,
        tractorRatePerHourInr: Double,
        upaadAdvanceGivenInr: Double
    ): DailyLabourTractorKhataResult {
        val workers = max(0, maleOrGeneralWorkers)
        val days = max(0.0, daysWorked)
        val personDays = workers * days
        val wage = max(0.0, dailyWagePerWorkerInr)
        val totalWages = personDays * wage

        val trHours = max(0.0, tractorHours)
        val trRate = max(0.0, tractorRatePerHourInr)
        val totalTractor = trHours * trRate
        val estDiesel = trHours * 4.2

        val gross = totalWages + totalTractor
        val upaad = max(0.0, upaadAdvanceGivenInr)
        val netBalance = gross - upaad

        return DailyLabourTractorKhataResult(
            workersCount = workers,
            daysWorked = days,
            totalPersonDays = personDays,
            dailyWagePerWorkerInr = wage,
            totalLabourWageInr = totalWages,
            tractorHours = trHours,
            tractorRatePerHourInr = trRate,
            totalTractorRentInr = totalTractor,
            estimatedDieselLiters = estDiesel,
            grossCombinedBillInr = gross,
            upaadAdvanceGivenInr = upaad,
            netBalancePayableInr = netBalance
        )
    }

    /**
     * Daily Land Broker / Real Estate Deal Commission, Token, Banakhat & Dastavej Schedule Calculator.
     */
    fun calculateLandDealBrokerage(
        totalDealAmountInr: Double,
        tokenAmountInr: Double,
        banakhatPercent: Double,
        buyerBrokeragePercent: Double,
        sellerBrokeragePercent: Double
    ): LandDealBrokerageResult {
        val deal = max(0.0, totalDealAmountInr)
        val token = tokenAmountInr.coerceIn(0.0, deal)
        val banPct = banakhatPercent.coerceIn(0.0, 90.0)
        val banAmount = max(0.0, (deal * (banPct / 100.0)) - token)
        val dastavejBalance = max(0.0, deal - token - banAmount)

        val bPct = buyerBrokeragePercent.coerceIn(0.0, 10.0)
        val sPct = sellerBrokeragePercent.coerceIn(0.0, 10.0)
        val buyerBrok = deal * (bPct / 100.0)
        val sellerBrok = deal * (sPct / 100.0)
        val totalBrok = buyerBrok + sellerBrok

        return LandDealBrokerageResult(
            totalDealAmountInr = deal,
            tokenAmountInr = token,
            banakhatPercent = banPct,
            banakhatAmountInr = banAmount,
            remainingOnDastavejInr = dastavejBalance,
            buyerBrokeragePercent = bPct,
            sellerBrokeragePercent = sPct,
            buyerBrokerageInr = buyerBrok,
            sellerBrokerageInr = sellerBrok,
            totalBrokeragePoolInr = totalBrok,
            totalBuyerOutflowInr = deal + buyerBrok,
            netSellerHandoverInr = max(0.0, deal - sellerBrok)
        )
    }

    /**
     * Daily Farm, Dairy (Milk) & Landowner Rojmel (Daily Income vs Expense Cashbook).
     */
    fun calculateDailyFarmDairyRojmel(
        dailyMilkLiters: Double,
        milkRatePerLiterInr: Double,
        otherDailyFarmIncomeInr: Double,
        cattleFeedAndCharaExpenseInr: Double,
        dieselAndFertilizerExpenseInr: Double,
        miscDailyExpenseInr: Double
    ): DailyFarmDairyRojmelResult {
        val liters = max(0.0, dailyMilkLiters)
        val rate = max(0.0, milkRatePerLiterInr)
        val dairyInc = liters * rate
        val otherInc = max(0.0, otherDailyFarmIncomeInr)
        val totalInc = dairyInc + otherInc

        val feedExp = max(0.0, cattleFeedAndCharaExpenseInr)
        val dieselExp = max(0.0, dieselAndFertilizerExpenseInr)
        val miscExp = max(0.0, miscDailyExpenseInr)
        val totalExp = feedExp + dieselExp + miscExp

        val netDaily = totalInc - totalExp
        val netMonthly = netDaily * 30.0

        return DailyFarmDairyRojmelResult(
            dailyMilkLiters = liters,
            milkRatePerLiterInr = rate,
            dailyMilkIncomeInr = dairyInc,
            otherDailyFarmIncomeInr = otherInc,
            totalDailyIncomeInr = totalInc,
            cattleFeedAndCharaExpenseInr = feedExp,
            dieselAndFertilizerExpenseInr = dieselExp,
            miscDailyExpenseInr = miscExp,
            totalDailyExpenseInr = totalExp,
            netDailyProfitInr = netDaily,
            projectedMonthlyNetProfitInr = netMonthly
        )
    }
}

