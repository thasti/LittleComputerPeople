package com.example.Demo_Subj;



import java.util.List;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by johannes on 24.11.2014.
 * modified by Karsten Becker 11.12.2014
 */
//TODO: need-ok?, sound, popup-ok?, user(from constructor->ok?),  function tick necessary? because animation outsourced to renderer
public class Item {
    //active or inactive
    /*
    private enum actstate {inactive,user,subject};
    private actstate active_state = actstate.inactive;
    */
    //active or inactive
    private int actstate = 0; //unused=0 ; user=1; subject=2

    private Integer id; //Item-ID
    private Integer picresource;
    private Integer picresourceAnimUser;
    private Integer picresourceAnimSubj;

    private Double xPos;
    private Double yPos;
    private String need;
    private Integer sound;
    private Integer popup;
    private Integer user;
    private Integer useTime = 200;
    private Integer usedTime = 0;
    private Integer a;//die IDs sind null wenn sie in der XML nicht existieren
    private Integer u;//die IDs sind null wenn sie in der XML nicht existieren
    //List<String> pics_for_animation;
    private Context context;

    private String itemName = "void";

    private int default_graphics_id; //default
    private int akt_graphics_id; //name of resource to draw
    // private static int holdAnimationCycles = 20; //time between animation steps

    //Lists contain graphics-ids for the animations
    //private List<Integer> itemAnimbySubject;
    //private List<Integer> itemAnimbyUser;



    //constructor
    public Item(Integer ID, Integer picresource, Double x, Double y, String need, Integer sound, Integer popup, Integer user, Integer pic_a, Integer pic_u, String itemName, Context context){


        this.id = ID;
        this.picresource = picresource;
        this.xPos = x;
        this.yPos = y;
        this.need = need;
        this.sound = sound;
        this.popup = popup;
        this.user = user;
        this.a = pic_a;
        this.u = pic_u;
        this.context = context;
        this.itemName = itemName;

        this.sound = context.getResources().getIdentifier("raw/"+itemName+"_s","raw", context.getPackageName());

    }
    //get resource-id from resource-name
    /*
    private int resNameToResID(String resourcename){

        return GlobalInformation.context.getResources().getIdentifier(resourcename , "drawable", GlobalInformation.context.getPackageName());
    }
    */



    //the renderer sets the activity state to 0 (unused) if the animation finished?!
    public void resetActstate(){
        this.actstate=0;

    }

    //item used by Subject
    public void useBySubject(){
        this.actstate = 2;
    }

    //item used by User
    public void useByUser(){
        this.actstate = 1;
    }

    //returns active-State for renderer
    public int getActstate(){
        return this.actstate;
    }
    //returns item-id
    public Integer getID(){
        return this.id;
    }

    //x-coordinate
    public Double getXPos(){
        return this.xPos;
    }
    //y-coordinate
    public Double getYPos(){
        return this.yPos;
    }
    //returns integer of resource to draw (instead of Bitmap)
    public Integer getPicresource(){
        return this.picresource;
    }

    public String getNeed(){
        return this.need;
    }

    public Integer getPopup(){
        return this.popup;
    }

    public Integer isUsed(){
        return actstate;
    }
    //
    public Integer getUser(){
        return this.user;
    }

    public Context getContext() {
        return this.context;
    }



    //tick
    public void tick(){
        //sets the graphic-id for the renderer
        if (this.actstate == 2){
           if(usedTime < useTime){
               usedTime++;
               //System.out.println("Waiting");
           }
           else
           {
               usedTime=0;
               this.resetActstate();
           }
        }
        else if (this.actstate == 1){
            //TODO: animation caused by user -->renderer
            if(usedTime < useTime){
                usedTime++;
            }
            else
            {
                usedTime=0;
                this.resetActstate();
            }
        }
        //if item is inactive
        else{
           //this.akt_graphics_id = this.default_graphics_id;
        }
    }


    // returns a list with string-names of the drawable resources
    //es wird erstmal ohne List gearbeitet
    /*public List<String> get_Pics_for_animation(){
        return this.pics_for_animation;
    }*/

    /*die ResourceIDs für aktiv durch user(u), oder aktiv durch subject(a)*/
    public Integer get_pic_a(){return this.a;}//die IDs sind null wenn sie in der XML nicht existieren
    public Integer get_pic_u(){return this.u;}//die IDs sind null wenn sie in der XML nicht existieren

    //adds drawable picture-ids for subject caused animation  in a list
    /*
    public void addGraphicsforSubjectUse(String graphicsname){
        itemAnimbySubject.add(resNameToResID(graphicsname));
    }
    */
    //adds drawable picture-id for user caused  animation in a list
    /*
    public void addGraphicsforUserUse(String graphicsname){
        itemAnimbyUser.add(resNameToResID(graphicsname));
    }
    */

    public int getSoundRes(){
        return sound;
    }

}
