package com.example.data.repository

import com.example.data.local.SurveyDao
import com.example.data.local.SurveyRecordEntity
import kotlinx.coroutines.flow.Flow

class SurveyRepository(private val surveyDao: SurveyDao) {
    val allRecords: Flow<List<SurveyRecordEntity>> = surveyDao.getAllRecords()

    suspend fun insert(record: SurveyRecordEntity) {
        surveyDao.insertRecord(record)
    }

    suspend fun deleteById(id: Int) {
        surveyDao.deleteRecordById(id)
    }

    suspend fun seedInitialRecordsIfEmpty() {
        if (surveyDao.getCount() == 0) {
            surveyDao.insertRecord(
                SurveyRecordEntity(
                    title = "Saurashtra Agricultural Plot #14 (Rajkot)",
                    category = "LAND_CONVERSION",
                    stateRegion = "Gujarat (Saurashtra / Central - 16 Guntha)",
                    primaryAreaSqFt = 43560.0,
                    summaryMetrics = "2.5000 Vigha • 40.00 Guntha • 4,840.00 Vaar • 1.0000 Acre",
                    detailedBreakdown = "Input: 2.5 Vigha (Gujarat 16-Guntha Standard)\n" +
                        "Total Sq.Feet: 43,560.00 sq.ft\n" +
                        "Total Sq.Meters: 4,046.86 sq.m\n" +
                        "Vaar / Gaj: 4,840.00 Vaar\n" +
                        "Guntha: 40.00 Guntha\n" +
                        "Land Valuation @ ₹12,50,000/Vigha: ₹31,25,000\n" +
                        "Est. Stamp Duty & Reg (5.9%): ₹1,84,375",
                    estimatedCostInr = 3309375.0,
                    plotSidesCsv = "220.0,198.0,220.0,198.0,296.0"
                )
            )
            surveyDao.insertRecord(
                SurveyRecordEntity(
                    title = "Corner Irregular Residential Plot (Surat)",
                    category = "IRREGULAR_PLOT",
                    stateRegion = "Gujarat (Saurashtra / Central - 16 Guntha)",
                    primaryAreaSqFt = 2218.28,
                    summaryMetrics = "2,218.28 Sq.Ft • 246.48 Vaar • 2.0370 Guntha • Saved 7.72 Sq.Ft Error",
                    detailedBreakdown = "Sides: North=50 ft, East=40 ft, South=56 ft, West=44 ft, Diagonal=68 ft\n" +
                        "Exact Heron Triangulation Area: 2,218.28 sq.ft\n" +
                        "Contractor Naive Avg Area: 2,226.00 sq.ft\n" +
                        "Overcharge Prevented: 7.72 sq.ft (₹27,020 @ ₹3,500/sq.ft)\n" +
                        "Vaar (Gaj): 246.48 Vaar | Guntha: 2.0370",
                    estimatedCostInr = 7763980.0,
                    plotSidesCsv = "50.0,40.0,56.0,44.0,68.0"
                )
            )
            surveyDao.insertRecord(
                SurveyRecordEntity(
                    title = "2BHK Vitrified Flooring & Paint BOQ",
                    category = "CONSTRUCTION_BOQ",
                    stateRegion = "All-India IS 456 / CPWD Standard",
                    primaryAreaSqFt = 720.0,
                    summaryMetrics = "48 Boxes (2x2 ft Tiles) • 18 Bags Cement • 92 Cu.Ft Sand • 28L Paint",
                    detailedBreakdown = "Flooring Area: 720.0 sq.ft + Skirting 64.0 sq.ft\n" +
                        "Vitrified Tiles (2x2 ft, 4/box, 7% wastage): 192 Tiles (48 Boxes)\n" +
                        "Bedding Mortar (1:6): 18 Bags Cement (50kg) + 92.4 Cu.Ft River Sand (0.92 Brass)\n" +
                        "Wall Putty (2 Coats): 134 kg | Primer: 16 Liters | Emulsion Paint: 28 Liters",
                    estimatedCostInr = 68450.0,
                    plotSidesCsv = ""
                )
            )
        }
    }
}
