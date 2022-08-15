package com.suicidesquid.syncswitch.data;

public class SwitchState {
    private boolean active;
    
    public SwitchState(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return this.active;
    }

    public void toggleActive(){
        this.active = !this.active;
    }
}
