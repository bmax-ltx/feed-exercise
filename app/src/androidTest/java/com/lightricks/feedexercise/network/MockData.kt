package com.lightricks.feedexercise.network

fun getHardcodedFeedResponse(): FeedResponse {
    val ls = ArrayList<MetadataItem>(3)
    val data0 = MetadataItem(
        "lensflare-unleash-the-power-of-nature.json",
        "01E18PGE1RYB3R9YF9HRXQ0ZSD",
        false,
        true,
        "lens-flare-template.json",
        "UnleashThePowerOfNatureThumbnail.jpg"
    )
    val data1 = MetadataItem(
        "accountingtravis.json",
        "01DX1RB94P35Q1A2W6AA5XCQZ9",
        false,
        true,
        "lightleaks-template.json",
        "AccountingTravisThumbnail.jpg"
    )

    val data2 = MetadataItem(
        "yeti.json",
        "01EAEFVPZ6MFJEMCA8XB06HB01",
        true,
        true,
        "fashion-template.json",
        "yeti-thumbnail.jpg"
    )
    ls.add(data0)
    ls.add(data1)
    ls.add(data2)
    return FeedResponse(ls)
}