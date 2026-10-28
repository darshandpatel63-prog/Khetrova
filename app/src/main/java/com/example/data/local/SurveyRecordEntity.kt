package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "survey_records")
data class SurveyRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "LAND_CONVERSION", "IRREGULAR_PLOT", "CONSTRUCTION_BOQ"
    val stateRegion: String,
    val primaryAreaSqFt: Double,
    val summaryMetrics: String,
    val detailedBreakdown: String,
    val estimatedCostInr: Double,
    val plotSidesCsv: String = "", // "north,east,south,west,diagonal" for CAD rendering in PDF
    val timestamp: Long = System.currentTimeMillis()
)
