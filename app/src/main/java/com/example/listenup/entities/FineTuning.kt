package com.example.listenup.entities

data class FineTuning(
    val dataset_duration_seconds: Any,
    val is_allowed_to_fine_tune: Boolean,
    val language: String,
    val manual_verification: Any,
    val manual_verification_requested: Boolean,
    val max_verification_attempts: Int,
    val message: Message,
    val next_max_verification_attempts_reset_unix_ms: Long,
    val progress: Progress,
    val slice_ids: Any,
    val state: State,
    val verification_attempts: Any,
    val verification_attempts_count: Int,
    val verification_failures: List<Any>
)