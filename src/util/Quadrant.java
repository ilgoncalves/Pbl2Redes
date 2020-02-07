/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Igor
 */
public class Quadrant {

    private String name;

    public Quadrant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * verifica pelo x, se o carro ainda esta naquele quadrante
     *
     * @param x
     * @return
     */
    public boolean stillQuadrantX(double x) {
        if (name.equals("d")) {
            if (x < 278) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("b")) {
            if (x > 230) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("a")) {
            if (x > 183) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("c")) {
            if (x < 230) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("D")) {
            if (x < 144) {
                return true;
            } else {
                return false;
            }
        } else if (x > 318) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * verifica pelo y, se o carro ainda esta naquele quadrante
     *
     * @param y
     * @return
     */
    public boolean stillQuadrantY(double y) {
        if (name.equals("d")) {
            if (y > 229) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("b")) {
            if (y > 183) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("a")) {
            if (y < 230) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("c")) {
            if (y < 280) {
                return true;
            } else {
                return false;
            }
        } else if (name.equals("C")) {
            if (y < 140) {
                return true;
            } else {
                return false;
            }
        } else {
            if (y > 316) {
                return true;
            } else {
                return false;
            }
        }
    }
}
