package repositories;

import lombok.Getter;

@Getter
public enum Taxon {
    all(0, null),
    domain  (0b01100000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, all),
    kingdom (0b00011100_00000000_00000000_00000000_00000000_00000000_00000000_00000000L, domain),
    type    (0b00000011_11111000_00000000_00000000_00000000_00000000_00000000_00000000L, kingdom),
    clazz   (0b00000000_00000111_11110000_00000000_00000000_00000000_00000000_00000000L, type),
    order   (0b00000000_00000000_00001111_11100000_00000000_00000000_00000000_00000000L, clazz),
    family  (0b00000000_00000000_00000000_00011111_11000000_00000000_00000000_00000000L, order),
    kind    (0b00000000_00000000_00000000_00000000_00111111_10000000_00000000_00000000L, family),
    specie  (0b00000000_00000000_00000000_00000000_00000000_01111111_11111111_11111111L, kind);

    private final long bitMask;
    private final long distance;
    private final Taxon parent;

    Taxon(long bitMask, Taxon parent) {

        this.bitMask = bitMask;
        this.parent = parent;
        this.distance = (~bitMask + 1) & bitMask;
    }
}
