package org.codebies.flakes4j.identifier;

public final class Identifiers {

    public final static Identifier IP_IDENTIFIER = SystemUtils::getMachineId;

    public final static Identifier RANDOM_IDENTIFIER = RandomUtils::getRandom16Bits;

}
