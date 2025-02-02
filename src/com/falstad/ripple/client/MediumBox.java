/*
    Copyright (C) 2017 by Paul Falstad

    This file is part of RippleGL.

    RippleGL is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    RippleGL is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RippleGL.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.falstad.ripple.client;

public class MediumBox extends RectDragObject {
	double speedIndex;
	double dampingIndex;
	
	MediumBox() {
		speedIndex = .25;
		dampingIndex = 1.;
	}
	
	MediumBox(StringTokenizer st) {
		super(st);
		speedIndex = new Double(st.nextToken()).doubleValue();
		if (st.hasMoreTokens()) {
			dampingIndex = new Double(st.nextToken()).doubleValue();
		}
		else dampingIndex = 1.; //to consist with old String token which has no dampingIndex information
	}

	void prepare() {
		RippleSim.drawMedium(topLeft.x, topLeft.y, topRight.x, topRight.y, 
				bottomLeft.x, bottomLeft.y,
				bottomRight.x, bottomRight.y,
				speedIndex, speedIndex,
				dampingIndex, dampingIndex);
	}
	
	// let people poke inside
	boolean hitTestInside(double x, double y) { return false; }

    public EditInfo getEditInfo(int n) {
        if (n == 0)
            return new EditInfo("Refractive Index (<=4)", Math.sqrt(1/speedIndex), 0, 1).
                setDimensionless();
		if (n == 1)
			return new EditInfo("Damping Index (0.9-1.1)", dampingIndex, 0, 1).
					setDimensionless();
        return null;
    }
    public void setEditValue(int n, EditInfo ei) {
        if (n == 0) {
        	speedIndex = getSpeedIndex(ei.value);
			ei.value = (speedIndex == 0) ? 0 : Math.sqrt(1.0 / speedIndex);
        	EditDialog.theEditDialog.updateValue(ei);
        }
		if (n == 1) {
			dampingIndex = ei.value;
			EditDialog.theEditDialog.updateValue(ei);
		}
    }

    static double getSpeedIndex(double n) {
		if (n == 0.)
			return 0.;
    	if (n < .85)
    		n = .85;
    	if (n > 4)
    		n = 4;
    	return 1/(n*n);
    }
    
	int getDumpType() { return 'm'; }
	String dump() { return super.dump() + " " + speedIndex + " " + dampingIndex; }
}
