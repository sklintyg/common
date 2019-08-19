/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.common.fkparent.pdf.eventhandlers;

/**
 * Extension of the generic FkAbstractPersonnummerEventHandler, overriding the positioning an on which pages to show the
 * personnummer on overflows pages.
 */
// CHECKSTYLE:OFF MagicNumber
public class FkOverflowPagePersonnummerEventHandlerImpl extends FkAbstractPersonnummerEventHandler {
    private final int activeFromPage;
    private final int activeToPage;
    private final float xOffset;
    private final float yOffset;

    public FkOverflowPagePersonnummerEventHandlerImpl(String personnummer) {
        this(personnummer, 5);
    }

    public FkOverflowPagePersonnummerEventHandlerImpl(String personnummer, int activeFromPage) {
        this(personnummer, activeFromPage, 999, 170f, 18f);
    }

    private FkOverflowPagePersonnummerEventHandlerImpl(String personnummer, int activeFromPage, int activeToPage, float xOffset,
            float yOffset) {
        super(personnummer);
        this.activeFromPage = activeFromPage;
        this.activeToPage = activeToPage;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    protected int getActiveFromPage() {
        return activeFromPage;
    }

    @Override
    protected int getActiveToPage() {
        return activeToPage;
    }

    @Override
    protected float getXOffset() {
        return xOffset;
    }

    @Override
    protected float getYOffset() {
        return yOffset;
    }
}
