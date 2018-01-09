package org.vincentzhang.max3d.interfaces;

import org.vincentzhang.max3d.core.Object3d;

/**
 * Using Actionscript 3 nomenclature for what are essentially "pass-thru" methods to an underlying ArrayList  
 */
public interface IObject3dContainer 
{
	void addChild(Object3d $child);
	void addChildAt(Object3d $child, int $index);
	boolean removeChild(Object3d $child);
	Object3d removeChildAt(int $index);
	Object3d getChildAt(int $index);
	Object3d getChildByName(String $string);
	int getChildIndexOf(Object3d $o);
	int numChildren();
}
