package com.example.listenup.entities

data class Voice(
    val available_for_tiers: List<Any>,
    val category: String,
    val created_at_unix: Any,
    val description: Any,
    val fine_tuning: FineTuning,
    val high_quality_base_model_ids: List<String>,
    val is_legacy: Boolean,
    val is_mixed: Boolean,
    val is_owner: Boolean,
    val labels: Labels,
    val name: String,
    val permission_on_resource: Any,
    val preview_url: String,
    val safety_control: Any,
    val samples: Any,
    val settings: Any,
    val sharing: Any,
    val verified_languages: List<Any>,
    val voice_id: String,
    val voice_verification: VoiceVerification
)