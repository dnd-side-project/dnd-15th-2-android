package com.qello.domain.model

data class InboxListing(
    val cards: List<InboxCard>,
    val chips: List<DirectionChip>,
)
