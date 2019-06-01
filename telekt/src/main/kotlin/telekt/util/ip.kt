package rocks.waffle.telekt.util.ip

@kotlin.ExperimentalUnsignedTypes
fun checkIp(addr: IpV4, net: IpV4, mask: IpV4Mask): Boolean =
    ((addr.value xor net.value) and mask.value) == 0U


@kotlin.ExperimentalUnsignedTypes
inline class IpV4(val value: UInt) {
    constructor(first: UByte, second: UByte, third: UByte, fourth: UByte) :
            this((first.toUInt() shl 24) + (second.toUInt() shl 16) + (third.toUInt() shl 8) + fourth.toUInt())

    companion object {
        fun parse(str: String): IpV4? = str.split('.').let {
            if (it.size != 4) null else IpV4(
                it[0].toUByte(),
                it[1].toUByte(),
                it[2].toUByte(),
                it[3].toUByte()
            )
        }
    }
}

@kotlin.ExperimentalUnsignedTypes
fun ipV4Mask(mask: Int): IpV4Mask = IpV4Mask(((0xffffffffU shr (32 - mask)) shl (32 - mask)))

@kotlin.ExperimentalUnsignedTypes
inline class IpV4Mask @Deprecated(
    "internal constructor",
    ReplaceWith("ipV4Mask(mask)", "rocks.waffle.telekt.dispatcher.ipV4Mask")
) constructor(val value: UInt)
