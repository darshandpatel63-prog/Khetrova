package com.example.domain

data class LegalDocumentItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val content: String
)

object PolyglotAndLegalData {

    val legalDocuments: List<LegalDocumentItem> = listOf(
        LegalDocumentItem(
            id = "privacy_policy",
            title = "1. Privacy Policy (Zero-Data Collection)",
            subtitle = "100% Offline Vault • Zero Dangerous Permissions • AdMob Compliant",
            content = "Daily Utility Lab operates on a strict Privacy-By-Design architecture. We do not collect, transmit, store, or share any personal identity documents, contacts, camera feeds, microphone audio, or banking data on any external server. All saved land surveys, 7/12 conversions, and construction BOQ records remain exclusively inside your device's local SQLite (Room) sandbox."
        ),
        LegalDocumentItem(
            id = "terms_of_service",
            title = "2. Terms of Service",
            subtitle = "Usage conditions for engineering & land measurement calculations",
            content = "By using Daily Utility Lab, you acknowledge that this software provides mathematical, geometric, agricultural, and engineering estimations based on Indian Standard (IS 456, IS 1443, IS 2212, IS 1661) formulas and regional/global revenue conversion tables. Users retain 100% ownership of all generated PDF reports and saved calculations."
        ),
        LegalDocumentItem(
            id = "disclaimer",
            title = "3. Engineering & Legal Land Survey Disclaimer",
            subtitle = "Important notice regarding regional revenue records (7/12, 8A, Khasra)",
            content = "Regional land units such as Vigha, Bigha, Katha, and Biswa vary by state, district, and historical revenue notifications (e.g., 16-Guntha Saurashtra Vigha vs 23.5-Guntha North Gujarat Vigha). While our mathematical formulas use exact Heron's triangulation and statutory conversion ratios, official land registration and boundary demarcation must be verified by a licensed Government Revenue Surveyor (DILR / Talati / Patwari) and structural engineer."
        ),
        LegalDocumentItem(
            id = "cookie_policy",
            title = "4. Cookie, Local Storage & AdMob Policy",
            subtitle = "Local SQLite storage & Google AdMob test/standard advertising disclosure",
            content = "Local storage is used strictly to persist your saved plot surveys, language preference, and 1-hour Rewarded Ad-Free timer on your device. This app displays Google AdMob Banner, Native, Interstitial, Rewarded, and App Open advertisements to keep all land and farming tools 100% free for every farmer and citizen."
        ),
        LegalDocumentItem(
            id = "about_page",
            title = "5. About Daily Utility Lab",
            subtitle = "Built for farmers (ખેડૂત), homeowners, civil engineers, masons & realtors",
            content = "Daily Utility Lab solves everyday field challenges in a single free toolkit: (1) Accurate Indian & Global country land unit conversion, (2) In-App Satellite Map & Stock-Chart line drawing with 3D parcel view, (3) 7/12 Hectare-Are-Sq.M revenue decoder, (4) Land partition among heirs, fencing & crop calculators, (5) 4-sided irregular plot CAD triangulation, and (6) Construction material Bill of Quantities (BOQ)."
        ),
        LegalDocumentItem(
            id = "contact_page",
            title = "6. Contact & Support Information",
            subtitle = "Technical support, bug reporting & regional unit feedback",
            content = "For technical questions, regional measurement standard additions, or feedback:\n• Maintainer: Daily Utility Lab Engineering Team\n• Support Channel: In-App Feedback & Issue Tracker\n• Response SLA: Within 48 business hours."
        ),
        LegalDocumentItem(
            id = "security_policy",
            title = "7. Security Policy (Device Sandbox)",
            subtitle = "Application sandboxing, PDF FileProvider isolation & tamper protection",
            content = "Security is enforced at the operating system level: (1) Zero dangerous runtime permissions requested in AndroidManifest.xml, (2) Exported PDF files are shared exclusively through AndroidX FileProvider with temporary read-only URI grants, and (3) Parameterized Room DAO queries prevent SQL injection."
        ),
        LegalDocumentItem(
            id = "acceptable_use",
            title = "8. Acceptable Use Policy",
            subtitle = "Ethical engineering, fair dealing & non-misrepresentation",
            content = "Users must not alter generated PDF reports to misrepresent plot boundaries or defraud buyers, tenants, or property owners. Always disclose the exact regional Vigha/Bigha square-foot standard used in any commercial quotation."
        ),
        LegalDocumentItem(
            id = "data_processing",
            title = "9. Data Processing Policy",
            subtitle = "100% client-side edge computation",
            content = "All mathematical computations—including Law of Cosines vertex projection, Heron's triangulation, 7/12 conversion, and PDF canvas rendering—execute 100% locally on the user's mobile CPU/GPU. Zero bytes of user calculation data are processed on external cloud infrastructure."
        ),
        LegalDocumentItem(
            id = "user_rights",
            title = "10. User Rights Policy (DPDP Act & GDPR Aligned)",
            subtitle = "Full right to inspect, export, and erase local records",
            content = "In alignment with India's Digital Personal Data Protection (DPDP) Act and global privacy standards, users exercise complete control over their data directly within the Saved Vault tab: instant inspection, portable PDF export, individual record deletion, and complete vault erasure."
        ),
        LegalDocumentItem(
            id = "copyright_notice",
            title = "11. Copyright & Intellectual Property Notice",
            subtitle = "Open mathematical algorithms & original UI/CAD implementation",
            content = "© 2026 Daily Utility Lab. All rights reserved. Mathematical formulas (Heron's formula, Bretschneider's formula, IS code mortar equations) are public domain scientific knowledge; the software architecture and CAD blueprint renderer are protected works."
        ),
        LegalDocumentItem(
            id = "community_guidelines",
            title = "12. Community & Field Sharing Guidelines",
            subtitle = "Best practices when sharing PDF BOQs with contractors & clients",
            content = "When sharing PDF estimates with masons, tile fitters, or land buyers on WhatsApp, include both the exact triangulated area and the diagonal measurement so both parties can verify the measurement on-site with a physical measuring tape."
        ),
        LegalDocumentItem(
            id = "refund_monetization",
            title = "13. Monetization, Free Access & Ad-Supported Policy",
            subtitle = "100% Free Core Utility • AdMob Supported • 1-Hour Rewarded Ad-Free Option",
            content = "All land conversion, 7/12 calculators, CAD plot triangulation, construction BOQ calculators, and offline PDF exports are provided 100% free of charge. Users can watch a single Rewarded Ad at any time to disable Interstitial and App Open popup ads for 1 full hour while non-intrusive Banner and Native ads remain active."
        )
    )
}
