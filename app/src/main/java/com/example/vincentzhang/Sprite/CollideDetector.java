package com.example.vincentzhang.Sprite;

import android.graphics.Rect;

import com.example.vincentzhang.Sprite.imgemanagement.BitmapMask;
import com.example.vincentzhang.Sprite.imgemanagement.Space4DTree;
import com.example.vincentzhang.Sprite.imgemanagement.Space4DTreeNode;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
        return dirtyFlag;
    }

    public static void setDirtyFlag(boolean dirtyFlag) {
        CollideDetector.dirtyFlag = dirtyFlag;
    }

    public CollideDetector(AbstractSprite collider, AbstractSprite target){
        this.colliderTree = collider.getSpace4DTree();
        this.colliderRowColumn = collider.getImgRowColumn();
        this.colliderScrRect = collider.getScrRect();

        this.targetTree = target.getSpace4DTree();
        this.targetRowColumn = target.getImgRowColumn();
        this.targetScrRect = target.getScrRect();
    }

    boolean detect(){
        BitmapMask targetBM = targetTree.getBmMask();
        Rect targetRect = targetTree.getRootRect(targetRowColumn);

        Rect colliderRect = colliderTree.getRootRect(colliderRowColumn);

        Queue<Space4DTreeNode> nodeQueue = new LinkedBlockingQueue<>();
        nodeQueue.offer(colliderTree.getRootNode(colliderRowColumn));

        while(!nodeQueue.isEmpty()){
            Space4DTreeNode curNode = nodeQueue.poll();
            Rect colliderNodeSpaceRect = curNode.getRect();
            // Map to screen Rect
            Rect scrNodeRect = Utilities.mapRect(colliderRect, colliderNodeSpaceRect, colliderScrRect);

            // Consider mapped left coordinate.
            Rect targetImgColliderRect = Utilities.mapScrToImageRect(targetRect, targetScrRect, scrNodeRect);

            Rect intersecRect = Utilities.intersectRect(targetImgColliderRect, targetRect);

            if(intersecRect == null){
                continue;
            }

            if(targetBM.getRangePixelCount(intersecRect) > 0){
                if(curNode.isLeafNode()){
                    return true;
                }

                Space4DTreeNode[] nodes = curNode.getChildren();
                boolean hasChildNode = false;
                for(Space4DTreeNode node : nodes){
                    if(node != null){
                        hasChildNode = true;
                        nodeQueue.offer(node);
                    }
                }

                if(!hasChildNode){
                    return true;
                }
            }
        }
        return false;
    }
}
