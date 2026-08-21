package com.example.easydrawer.file

import java.io.InputStream

data class PsdLayerData(
    val name: String,
    val visible: Boolean,
    val opacity: Float,
    val top: Int,
    val left: Int,
    val bottom: Int,
    val right: Int,
    val pixels: IntArray
)

data class PsdDocumentData(
    val width: Int,
    val height: Int,
    val layers: List<PsdLayerData>
)

class PsdReader(
    private val input: InputStream
) {

    fun read(): PsdDocumentData {

        readHeader()

        readSection()

        readSection()

        val layerSectionLength =
            readInt()

        val layerSection =
            readBytes(
                layerSectionLength
            )

        return readLayers(
            layerSection
        )
    }

    private var width = 0
    private var height = 0
    private var channels = 0

    private fun readHeader() {

        val signature =
            readAscii(4)

        require(
            signature == "8BPS"
        ) {
            "Not a PSD file"
        }

        val version =
            readShort()

        require(
            version == 1
        ) {
            "Unsupported PSD version: $version"
        }

        skip(6)

        channels =
            readShort()

        height =
            readInt()

        width =
            readInt()

        val depth =
            readShort()

        require(
            depth == 8
        ) {
            "Only 8 bit PSD is supported"
        }

        val colorMode =
            readShort()

        require(
            colorMode == 3
        ) {
            "Only RGB PSD is supported"
        }

        require(
            width > 0 &&
                    height > 0
        )

        require(
            width <= 30000 &&
                    height <= 30000
        )
    }

    private fun readSection() {

        val length =
            readInt()

        if (length > 0) {
            skip(length)
        }
    }

    private fun readLayers(
        data: ByteArray
    ): PsdDocumentData {

        val reader =
            ByteArrayReader(data)

        val layerInfoLength =
            reader.readInt()

        if (layerInfoLength <= 0) {

            return PsdDocumentData(
                width = width,
                height = height,
                layers = emptyList()
            )
        }

        val layerInfo =
            reader.readBytes(
                layerInfoLength
            )

        val layerReader =
            ByteArrayReader(layerInfo)

        var layerCount =
            layerReader.readShort()

        if (layerCount < 0) {
            layerCount = -layerCount
        }

        val records =
            mutableListOf<LayerRecord>()

        repeat(layerCount) {

            records.add(
                readLayerRecord(
                    layerReader
                )
            )
        }

        /*
         * Channel data comes directly after
         * all layer records.
         */
        val layers =
            records.map { record ->

                val pixelCount =
                    record.width *
                            record.height

                val red =
                    readChannel(
                        layerReader,
                        pixelCount
                    )

                val green =
                    readChannel(
                        layerReader,
                        pixelCount
                    )

                val blue =
                    readChannel(
                        layerReader,
                        pixelCount
                    )

                val alpha =
                    readChannel(
                        layerReader,
                        pixelCount
                    )

                val pixels =
                    IntArray(
                        pixelCount
                    )

                for (i in 0 until pixelCount) {

                    val a =
                        alpha[i]

                    val r =
                        red[i]

                    val g =
                        green[i]

                    val b =
                        blue[i]

                    pixels[i] =
                        (
                                (a shl 24) or
                                        (r shl 16) or
                                        (g shl 8) or
                                        b
                                )
                }

                PsdLayerData(
                    name = record.name,
                    visible = record.visible,
                    opacity = record.opacity,
                    top = record.top,
                    left = record.left,
                    bottom = record.bottom,
                    right = record.right,
                    pixels = pixels
                )
            }

        /*
         * PSD stores layers bottom -> top
         * in our writer's representation.
         *
         * Return them in document order.
         */
        return PsdDocumentData(
            width = width,
            height = height,
            layers = layers
        )
    }

    private fun readLayerRecord(
        reader: ByteArrayReader
    ): LayerRecord {

        val top =
            reader.readInt()

        val left =
            reader.readInt()

        val bottom =
            reader.readInt()

        val right =
            reader.readInt()

        val channelCount =
            reader.readShort()

        val channelIds =
            mutableListOf<Int>()

        val channelLengths =
            mutableListOf<Int>()

        repeat(channelCount) {

            channelIds.add(
                reader.readShort()
            )

            channelLengths.add(
                reader.readInt()
            )
        }

        val signature =
            reader.readAscii(4)

        require(
            signature == "8BIM"
        )

        val blendMode =
            reader.readAscii(4)

        require(
            blendMode == "norm"
        ) {
            "Unsupported blend mode: $blendMode"
        }

        val opacityByte =
            reader.readUnsignedByte()

        val opacity =
            opacityByte / 255f

        reader.readUnsignedByte()

        val flags =
            reader.readUnsignedByte()

        reader.readUnsignedByte()

        val extraLength =
            reader.readInt()

        val extraStart =
            reader.position

        val maskLength =
            reader.readInt()

        if (maskLength > 0) {
            reader.skip(maskLength)
        }

        val blendingRangesLength =
            reader.readInt()

        if (blendingRangesLength > 0) {
            reader.skip(
                blendingRangesLength
            )
        }

        val nameLength =
            reader.readUnsignedByte()

        val nameBytes =
            reader.readBytes(
                nameLength
            )

        val name =
            String(
                nameBytes,
                Charsets.UTF_8
            )

        val consumed =
            reader.position -
                    extraStart

        val remaining =
            extraLength -
                    consumed

        if (remaining > 0) {
            reader.skip(remaining)
        }

        return LayerRecord(
            top = top,
            left = left,
            bottom = bottom,
            right = right,
            visible =
                (flags and 2) == 0,
            opacity = opacity,
            name = name,
            channelIds = channelIds,
            channelLengths = channelLengths
        )
    }

    private fun readChannel(
        reader: ByteArrayReader,
        pixelCount: Int
    ): IntArray {

        val compression =
            reader.readShort()

        require(
            compression == 0
        ) {
            "Only RAW PSD compression is supported"
        }

        val result =
            IntArray(
                pixelCount
            )

        for (i in 0 until pixelCount) {

            result[i] =
                reader.readUnsignedByte()
        }

        return result
    }

    private data class LayerRecord(
        val top: Int,
        val left: Int,
        val bottom: Int,
        val right: Int,
        val visible: Boolean,
        val opacity: Float,
        val name: String,
        val channelIds: List<Int>,
        val channelLengths: List<Int>
    ) {

        val width: Int
            get() = right - left

        val height: Int
            get() = bottom - top
    }

    private fun readByte(): Int {

        val value =
            input.read()

        require(
            value >= 0
        ) {
            "Unexpected end of PSD"
        }

        return value
    }

    private fun readUnsignedByte(): Int {
        return readByte()
    }

    private fun readShort(): Int {

        val b1 =
            readByte()

        val b2 =
            readByte()

        return (
                (b1 shl 8) or
                        b2
                ).toShort().toInt()
    }

    private fun readInt(): Int {

        val b1 =
            readByte()

        val b2 =
            readByte()

        val b3 =
            readByte()

        val b4 =
            readByte()

        return (
                (b1 shl 24) or
                        (b2 shl 16) or
                        (b3 shl 8) or
                        b4
                )
    }

    private fun readAscii(
        length: Int
    ): String {

        return String(
            readBytes(length),
            Charsets.US_ASCII
        )
    }

    private fun readBytes(
        length: Int
    ): ByteArray {

        require(
            length >= 0
        )

        val result =
            ByteArray(length)

        var offset = 0

        while (
            offset < length
        ) {

            val count =
                input.read(
                    result,
                    offset,
                    length - offset
                )

            require(
                count > 0
            ) {
                "Unexpected end of PSD"
            }

            offset += count
        }

        return result
    }

    private fun skip(
        length: Int
    ) {

        require(
            length >= 0
        )

        var remaining =
            length.toLong()

        while (
            remaining > 0
        ) {

            val skipped =
                input.skip(
                    remaining
                )

            if (skipped <= 0) {
                readByte()
                remaining--
            } else {
                remaining -= skipped
            }
        }
    }
}

private class ByteArrayReader(
    private val data: ByteArray
) {

    var position: Int = 0
        private set

    fun readByte(): Int {

        requireAvailable(1)

        return data[
            position++
        ].toInt() and 0xFF
    }

    fun readUnsignedByte(): Int {
        return readByte()
    }

    fun readShort(): Int {

        val b1 =
            readByte()

        val b2 =
            readByte()

        return (
                (b1 shl 8) or
                        b2
                ).toShort().toInt()
    }

    fun readInt(): Int {

        val b1 =
            readByte()

        val b2 =
            readByte()

        val b3 =
            readByte()

        val b4 =
            readByte()

        return (
                (b1 shl 24) or
                        (b2 shl 16) or
                        (b3 shl 8) or
                        b4
                )
    }

    fun readAscii(
        length: Int
    ): String {

        return String(
            readBytes(length),
            Charsets.US_ASCII
        )
    }

    fun readBytes(
        length: Int
    ): ByteArray {

        require(
            length >= 0
        )

        requireAvailable(length)

        val result =
            data.copyOfRange(
                position,
                position + length
            )

        position += length

        return result
    }

    fun skip(
        length: Int
    ) {

        require(
            length >= 0
        )

        requireAvailable(length)

        position += length
    }

    private fun requireAvailable(
        count: Int
    ) {

        require(
            position + count <= data.size
        ) {
            "Unexpected end of PSD layer data"
        }
    }
}