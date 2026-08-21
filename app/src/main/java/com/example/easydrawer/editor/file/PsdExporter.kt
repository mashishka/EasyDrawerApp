package com.example.easydrawer.file

import android.graphics.Bitmap
import com.example.easydrawer.editor.Document
import java.io.OutputStream

class PsdExporter {

    private val layerRenderer =
        LayerRenderer()

    fun export(
        document: Document,
        output: OutputStream
    ) {

        val bitmaps =
            document.layers.map { layer ->

                layerRenderer.render(
                    layer = layer,
                    width = document.width,
                    height = document.height
                )
            }

        try {

            PsdWriter(output).apply {

                writeHeader(
                    width = document.width,
                    height = document.height
                )

                writeEmptyColorModeData()

                writeEmptyImageResources()

                writeLayers(
                    document = document,
                    bitmaps = bitmaps
                )

                writeCompositeImage(
                    bitmap = mergeBitmaps(
                        bitmaps = bitmaps,
                        width = document.width,
                        height = document.height
                    )
                )

                flush()
            }

        } finally {

            bitmaps.forEach {
                it.recycle()
            }
        }
    }

    private fun mergeBitmaps(
        bitmaps: List<Bitmap>,
        width: Int,
        height: Int
    ): Bitmap {

        val result =
            Bitmap.createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            )

        val canvas =
            android.graphics.Canvas(result)

        bitmaps.forEach { bitmap ->

            canvas.drawBitmap(
                bitmap,
                0f,
                0f,
                null
            )
        }

        return result
    }
}

private class PsdWriter(
    private val output: OutputStream
) {

    fun writeHeader(
        width: Int,
        height: Int
    ) {

        require(width in 1..30000)
        require(height in 1..30000)

        writeAscii("8BPS")

        writeShort(1)

        writeBytes(
            ByteArray(6)
        )

        // RGB
        writeShort(4)

        writeInt(height)

        writeInt(width)

        // 8 bit/channel
        writeShort(8)

        // RGB
        writeShort(3)
    }

    fun writeEmptyColorModeData() {

        writeInt(0)
    }

    fun writeEmptyImageResources() {

        writeInt(0)
    }

    private fun writeByte(
        value: Int
    ) {
        output.write(
            value and 0xFF
        )
    }

    private fun writeBitmapChannel(
        bitmap: Bitmap,
        selector: (Int) -> Int
    ) {
        val pixels =
            IntArray(
                bitmap.width *
                        bitmap.height
            )

        bitmap.getPixels(
            pixels,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )

        pixels.forEach { pixel ->
            writeByte(
                selector(pixel)
            )
        }
    }

    fun writeLayers(
        document: Document,
        bitmaps: List<Bitmap>
    ) {
        val layerInfoStream =
            java.io.ByteArrayOutputStream()

        val layerWriter =
            PsdWriter(layerInfoStream)

        /*
         * Number of layers.
         */
        layerWriter.writeShort(
            bitmaps.size
        )

        /*
         * Layer Records.
         *
         * PSD stores layers from bottom to top
         * in the file representation, so we reverse
         * our UI order here.
         */
        document.layers
            .asReversed()
            .zip(bitmaps.asReversed())
            .forEach { (layer, bitmap) ->

                /*
                 * Layer rectangle:
                 *
                 * top
                 * left
                 * bottom
                 * right
                 */
                layerWriter.writeInt(0)
                layerWriter.writeInt(0)
                layerWriter.writeInt(bitmap.height)
                layerWriter.writeInt(bitmap.width)

                /*
                 * RGBA channels.
                 */
                layerWriter.writeShort(4)

                /*
                 * Red
                 */
                layerWriter.writeShort(0)
                layerWriter.writeInt(
                    bitmap.width *
                            bitmap.height +
                            2
                )

                /*
                 * Green
                 */
                layerWriter.writeShort(1)
                layerWriter.writeInt(
                    bitmap.width *
                            bitmap.height +
                            2
                )

                /*
                 * Blue
                 */
                layerWriter.writeShort(2)
                layerWriter.writeInt(
                    bitmap.width *
                            bitmap.height +
                            2
                )

                /*
                 * Transparency / Alpha
                 */
                layerWriter.writeShort(-1)
                layerWriter.writeInt(
                    bitmap.width *
                            bitmap.height +
                            2
                )

                /*
                 * Blend mode.
                 */
                layerWriter.writeAscii("8BIM")
                layerWriter.writeAscii("norm")

                /*
                 * Opacity.
                 */
                layerWriter.writeByte(
                    (
                            layer.opacity * 255f
                            )
                        .toInt()
                        .coerceIn(0, 255)
                )

                /*
                 * Clipping.
                 */
                layerWriter.writeByte(0)

                /*
                 * Flags.
                 *
                 * 0 = visible
                 * 2 = hidden
                 */
                layerWriter.writeByte(
                    if (layer.visible) 0 else 2
                )

                /*
                 * Filler.
                 */
                layerWriter.writeByte(0)

                /*
                 * Extra data.
                 */
                val nameBytes =
                    layer.name.toByteArray(
                        Charsets.UTF_8
                    )

                val nameLength =
                    nameBytes
                        .size
                        .coerceAtMost(255)

                val paddedNameLength =
                    (
                            nameLength + 1 + 3
                            ) / 4 * 4

                val extraLength =
                    4 +
                            4 +
                            paddedNameLength

                layerWriter.writeInt(
                    extraLength
                )

                /*
                 * Layer mask data.
                 */
                layerWriter.writeInt(0)

                /*
                 * Blending ranges.
                 */
                layerWriter.writeInt(0)

                /*
                 * Layer name.
                 */
                layerWriter.writeByte(
                    nameLength
                )

                layerWriter.writeBytes(
                    nameBytes.copyOf(nameLength)
                )

                repeat(
                    paddedNameLength -
                            nameLength -
                            1
                ) {
                    layerWriter.writeByte(0)
                }
            }

        /*
         * Channel pixel data.
         *
         * Each channel starts with its own
         * compression field.
         *
         * 0 = RAW.
         */
        document.layers
            .asReversed()
            .zip(bitmaps.asReversed())
            .forEach { (_, bitmap) ->

                /*
                 * Red
                 */
                layerWriter.writeShort(0)

                layerWriter.writeBitmapChannel(
                    bitmap
                ) { pixel ->
                    (pixel shr 16) and 0xFF
                }

                /*
                 * Green
                 */
                layerWriter.writeShort(0)

                layerWriter.writeBitmapChannel(
                    bitmap
                ) { pixel ->
                    (pixel shr 8) and 0xFF
                }

                /*
                 * Blue
                 */
                layerWriter.writeShort(0)

                layerWriter.writeBitmapChannel(
                    bitmap
                ) { pixel ->
                    pixel and 0xFF
                }

                /*
                 * Alpha
                 */
                layerWriter.writeShort(0)

                layerWriter.writeBitmapChannel(
                    bitmap
                ) { pixel ->
                    (pixel ushr 24) and 0xFF
                }
            }

        val layerInfo =
            layerInfoStream.toByteArray()

        /*
         * Global Layer Mask Info is empty.
         */
        val globalMaskSize = 4

        /*
         * There is no global additional layer info.
         */
        val additionalInfoSize = 4

        /*
         * Content of the outer
         * Layer and Mask Information section:
         *
         *   4 bytes  Layer Info length
         *   N bytes  Layer Info
         *   4 bytes  Global Mask length
         *   4 bytes  Global Additional Info length
         */
        val sectionLength =
            4 +
                    layerInfo.size +
                    globalMaskSize +
                    additionalInfoSize

        /*
         * Outer section length.
         */
        writeInt(sectionLength)

        /*
         * Layer Info length.
         */
        writeInt(
            layerInfo.size
        )

        /*
         * Layer Info.
         */
        writeBytes(
            layerInfo
        )

        /*
         * Global Layer Mask Info length.
         */
        writeInt(0)

        /*
         * No global additional layer info.
         */
        writeInt(0)
    }

    fun writeCompositeImage(
        bitmap: Bitmap
    ) {

        /*
         * RAW compression.
         */
        writeShort(0)

        /*
         * PSD composite image содержит
         * каналы целиком.
         *
         * Пишем:
         *
         * R
         * G
         * B
         * Alpha
         */

        writeChannel(
            bitmap
        ) { pixel ->

            (pixel shr 16) and 0xFF
        }

        writeChannel(
            bitmap
        ) { pixel ->

            (pixel shr 8) and 0xFF
        }

        writeChannel(
            bitmap
        ) { pixel ->

            pixel and 0xFF
        }

        writeChannel(
            bitmap
        ) { pixel ->

            (pixel ushr 24) and 0xFF
        }
    }

    private fun writeChannel(
        bitmap: Bitmap,
        selector: (Int) -> Int
    ) {

        val pixels =
            IntArray(
                bitmap.width *
                        bitmap.height
            )

        bitmap.getPixels(
            pixels,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )

        pixels.forEach { pixel ->

            output.write(
                selector(pixel)
            )
        }
    }

    fun flush() {

        output.flush()
    }

    private fun writeAscii(
        value: String
    ) {

        output.write(
            value.toByteArray(
                Charsets.US_ASCII
            )
        )
    }

    private fun writeBytes(
        bytes: ByteArray
    ) {

        output.write(bytes)
    }

    private fun writeShort(
        value: Int
    ) {

        output.write(
            (value shr 8) and 0xFF
        )

        output.write(
            value and 0xFF
        )
    }

    private fun writeInt(
        value: Int
    ) {

        output.write(
            (value shr 24) and 0xFF
        )

        output.write(
            (value shr 16) and 0xFF
        )

        output.write(
            (value shr 8) and 0xFF
        )

        output.write(
            value and 0xFF
        )
    }
}