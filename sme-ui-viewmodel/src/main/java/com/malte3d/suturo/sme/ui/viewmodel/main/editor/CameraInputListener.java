package com.malte3d.suturo.sme.ui.viewmodel.main.editor;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CameraInputListener implements AnalogListener, ActionListener {

    private final CameraNode camNode;
    private final Vector3f moveDir = new Vector3f();
    private final Vector3f rotateDir = new Vector3f();
    private final float moveSpeed = 10f;
    private final float rotateSpeed = 3f;

    public void update(float tpf) {
        
        // Move the camera
        camNode.move(camNode.getWorldRotation().mult(moveDir.mult(moveSpeed * tpf)));

        // Rotate the camera
        Quaternion rotation = new Quaternion();
        rotation.fromAngles(rotateDir.y * rotateSpeed * tpf, rotateDir.x * rotateSpeed * tpf, 0);
        camNode.rotate(rotation);
    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("MoveForward") && !isPressed) {
            moveDir.z = 0;
        } else if (name.equals("MoveBackward") && !isPressed) {
            moveDir.z = 0;
        } else if (name.equals("MoveLeft") && !isPressed) {
            moveDir.x = 0;
        } else if (name.equals("MoveRight") && !isPressed) {
            moveDir.x = 0;
        } else if (name.equals("RotateLeft") && !isPressed) {
            rotateDir.y = 0;
        } else if (name.equals("RotateRight") && !isPressed) {
            rotateDir.y = 0;
        } else if (name.equals("RotateUp") && !isPressed) {
            rotateDir.x = 0;
        } else if (name.equals("RotateDown") && !isPressed) {
            rotateDir.x = 0;
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        switch (name) {
            case "MoveForward" -> moveDir.z = -1;
            case "MoveBackward" -> moveDir.z = 1;
            case "MoveLeft" -> moveDir.x = -1;
            case "MoveRight" -> moveDir.x = 1;
            case "RotateLeft" -> rotateDir.y = -1;
            case "RotateRight" -> rotateDir.y = 1;
            case "RotateUp" -> rotateDir.x = 1;
            case "RotateDown" -> rotateDir.x = -1;
        }
    }
}
