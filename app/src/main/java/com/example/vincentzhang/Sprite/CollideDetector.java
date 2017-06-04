package com.example.vincentzhang.Sprite;

import android.graphics.Rect;

import com.example.vincentzhang.Sprite.imgemanagement.BitmapMask;
import com.example.vincentzhang.Sprite.imgemanagement.Space4DTree;
import com.example.vincentzhang.Sprite.imgemanagement.Space4DTreeNode;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by VincentZhang on 5/11/2017.
 */

public class CollideDetector {
    private static boolean dirtyFlag = true;

    private Space4DTree colliderTree;
    private Vector2D colliderRowColumn;
    private Rect colliderScrRect;

    private Space4DTree targetTree;
    private Vector2D targetRowColumn;
    private Rect targetScrRect;

    public static boolean isDirtyFlag() {
        // return dirtyFlag;
        return true;
    }

    public static void setDirtyFlag(boolean dirtyFlag) {
        CollideDetector.dirtyFlag = dirtyFlag;
    }

    public CollideDetector(AbstractCollidableSprite collider, AbstractCollidableSprite target){
        this.colliderTree = collider.getSpace4DTree();
        this.colliderRowColumn = collider.getImgRowColumn();
        this.colliderScrRect = collider.getScrRect();

        this.targetTree = target.getSpace4DTree();
        this.targetRowColumn = target.getImgRowColumn();
        this.targetScrRect = target.getScrRect();
    }

    boolean detect(){
        int detectCount = 0;
        BitmapMask targetBM = targetTree.getBmMask();
        Rect targetRect = targetTree.getRootRect(targetRowColumn);

        Rect colliderRect = colliderTree.getRootRect(colliderRowColumn);

        // Queue<Space4DTreeNode> nodeQueue = new LinkedBlockingQueue<>();
        Stack<Space4DTreeNode> nodeStack = new Stack<>();
        nodeStack.push(colliderTree.getRootNode(colliderRowColumn));

        while(!nodeStack.isEmpty()){
            detectCount++;
            Space4DTreeNode curNode = nodeStack.pop();
            Rect colliderNodeSpaceRect = curNode.getRect();
            // Map to screen Rect
            Rect scrNodeRect = Utilities.mapRect(colliderRect, colliderNodeSpaceRect, colliderScrRect);

            // Less than 4 pixels, regard as collided.
            if(scrNodeRect.width() <= 2 && scrNodeRect.height() <= 2){
                // Log.i("Effort 1","Detected:" + detectCount + " times");
                return true;
            }

            // Consider mapped left coordinate.
            Rect targetImgColliderRect = Utilities.mapScrToImageRect(targetRect, targetScrRect, scrNodeRect);

            Rect intersecRect = Utilities.intersectRect(targetImgColliderRect, targetRect);

            if(intersecRect == null){
                continue;
            }

            if(targetBM.getRangePixelCount(intersecRect) > 0){
                if(curNode.isLeafNode()){
                    // Log.i("Effort 2","Detected:" + detectCount + " times");
                    return true;
                }

                ArrayList<Space4DTreeNode> nodes = curNode.getChildren();
                for(Space4DTreeNode node : nodes){
                    nodeStack.push(node);
                }
            }
        }
        // Log.i("Effort 3","Not Detected collide! Effort:" + detectCount + " times");
        return false;
    }
}
