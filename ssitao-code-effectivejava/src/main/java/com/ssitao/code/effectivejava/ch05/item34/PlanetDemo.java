package com.ssitao.code.effectivejava.ch05.item34;

/**
 * Demo for Planet enum - showing enum advantages over int constants
 */
public class PlanetDemo {
    public static void main(String[] args) {
        System.out.println("=== Planet Enum Demo ===\n");

        // Weight on Earth vs other planets
        double earthWeight = 185.0;
        double mass = earthWeight / Planet.EARTH.surfaceGravity();

        for (Planet p : Planet.values()) {
            System.out.printf("%s: %.2f lbs%n", p, p.surfaceWeight(mass));
        }
    }
}
