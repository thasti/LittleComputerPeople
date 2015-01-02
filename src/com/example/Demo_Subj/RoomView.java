package com.example.Demo_Subj;

/**
 * 22.11.2014 - File created - JS
 * 29.11.2014 - taken over (refactored) - SB
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.*;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoomView extends View {

    private Context ctx;
    private Paint p;
    //private Subject subject;
    private List<Room> roomList;
    private Room drawRoom;
    private Bitmap drawRoomB;
    private int lastRoom = -1;          //Auf einen für die Räume ungültigen Wert vorinitialisieren

    //Variablen für die (noch einzufügenden) verschiedenen Animationen
    private int animation = 0;          //0 = laufen, 1 = ... usw.


    //Eigenschaften aus Subject
    /*****************************************************************************************/
    //aktuelle Position
    private float xPos;
    private float yPos;
    private int aktRoomID = 1;

    //Ziel
    private float xDest;
    private int destRoomID;

    //Bildverweise
    private Bitmap subjStandBitmap;
    private Bitmap subjStandBitmapInv;

    private List<Bitmap> subjectWalk;
    private List<Bitmap> subjectWalkInv;

    private Bitmap aktBitmap;

    private int listPointerWalk = 0;
    private int holdAnimation = 0;
    private static int holdAnimationCycles = 20;

    private Intelligence intel;
    /*****************************************************************************************/

    private Sound sound;
    private Resources resources;

    private List<Integer> itemList;                 //Existiert temporär, bis der XML-Parser eingebunden wurde
    private Item item;                              //Existiert temporär zum Test

    public RoomView(Context c) {
        super(c);

        this.ctx = c;
        this.p = null;


        //Aus RoomActivity und Subject
        /*****************************************************************************************/

        resources = getResources();
        fillWalkingArrayLists(resources);
        fillItemList();
        fillRoomList(resources);
        //Zusätzlich müssen hier Methodenaufrufe zum Laden der Objektanimationen hin
        //fillObjectAnimList(resources);

        float canvasx = (float) GlobalInformation.getScreenWidth();
        float canvasy = (float) GlobalInformation.getScreenHeight();
        float bitmapx = (float) subjStandBitmap.getWidth();
        float bitmapy = (float) subjStandBitmap.getHeight();
        float posX = ((canvasx/2) - (bitmapx / 2));
        float posY = (((canvasy/2) - (bitmapy / 2)) + (canvasy/15));
        setDefaultKoords(posX, posY, aktRoomID);

        GlobalInformation.setCurrentRoom(aktRoomID);
        GlobalInformation.setRoomList(roomList);

        intel = new Intelligence();

        /*****************************************************************************************/

        sound = new Sound(this.ctx);
    }

    public int getAktRoomID(){
        return aktRoomID;
    }

    private void fillWalkingArrayLists(Resources resources){                                        //Existiert temporär, wird ins Subjekt verschoben

        List<Bitmap> subjectWalkingList;                                                                    //Animation des Subjekts und Laufens laden
        Bitmap bm;                                                                                          //temporäre Bitmap Variable zum spiegeln und befüllen der Arrays

        Bitmap subjectBStand = BitmapFactory.decodeResource(resources, R.drawable.subjekt);
        subjectWalkingList = new ArrayList<Bitmap>();
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_1));
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_2));
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_3));
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_4));

        subjStandBitmap = Bitmap.createScaledBitmap(subjectBStand, 170, 330, false);
        subjStandBitmapInv = mirrorBitmap(subjStandBitmap);

        aktBitmap = subjStandBitmap;

        subjectWalk = new ArrayList<Bitmap>();                                                              //Arrays für die einzelnen Bilder der Animation (vorwärts und rückwärts)
        subjectWalkInv = new ArrayList<Bitmap>();

        for (int i = 0; i <= (subjectWalkingList.size() - 1); i++){                                         //Vorwärtsarray befüllen
            bm = subjectWalkingList.get(i);
            subjectWalk.add(i,  Bitmap.createScaledBitmap(bm, 170, 330, false));
        }

        for (int i = 0; i <= (subjectWalk.size() - 1); i++){                                                //Rückwärtsarray ist gespiegeltes Vorwärtsarray
            bm = subjectWalk.get(i);
            bm = this.mirrorBitmap(bm);
            subjectWalkInv.add(i, bm);
        }

    }

    private void setDefaultKoords(float xDef, float yDef, int room) {
        xPos = xDef;
        yPos = yDef;
        aktRoomID = room;

        xDest = xDef;
        destRoomID = room;
    }

    private void fillRoomList(Resources resources){                                             //Existiert temporär, bis der XML-Parser eingebunden wurde
        World.setRoom(1, new Room(1, R.drawable.schlafzimmer, 0.0, 0.0, null, this.ctx));
        World.setRoom(2, new Room(2, R.drawable.wohnzimmer, 0.0, 0.0, itemList, this.ctx));
        //roomList.add(new Room(BitmapFactory.decodeResource(resources, R.drawable.schlafzimmer), 1, this.ctx));
        //roomList.add(new Room(BitmapFactory.decodeResource(resources, R.drawable.wohnzimmer), 2, this.ctx));
    }

    private void fillObjectAnimList(Resources resources){                                       //Existiert temporär, bis der XML-Parser eingebunden wurde
        //eine globale Liste für Objektanimationen wird hier befüllt
        //die Variable animation steht für eine Animation, die ausgeführt werden soll
        //die switch-case Verzweigung in der Methode onDraw muss zusätzlich erweitert werden
    }

    private void fillItemList(){                                                                //Existiert temporär, bis der XML-Parser eingebunden wurde
        itemList = new ArrayList<Integer>();
        item = new Item(1, R.drawable.pflanze, GlobalInformation.getScreenWidth()*0.5, GlobalInformation.getScreenHeight()*0.5, null, R.raw.shower_s, null, null, null, ctx);
        World.setItem(1, item);
        itemList.add(World.getItemById(1).getPicresource());
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        // find out which room to draw
        //TODO should be replaced by a proper find() in the list

        if(lastRoom != aktRoomID) {                                                 //hat sich lastroom gegenüber dem letzten Aufruf geändert? wenn ja dann lade den aktuellen Raum
            for (Iterator<Room> iter = World.getAllRooms().iterator(); iter.hasNext(); ) {
                Room room = iter.next();
                if (room.getID() == aktRoomID) {
                    drawRoom = room;
                    drawRoomB = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(resources, room.getPicResource()),
                            GlobalInformation.getScreenWidth(),
                            GlobalInformation.getScreenHeight(),
                            false);
                    lastRoom = room.getID();
                }
            }
        }

        if(lastRoom != GlobalInformation.getCurrentRoom()){
            drawRoom = World.getRoomById(GlobalInformation.getCurrentRoom());
            drawRoomB = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(resources, drawRoom.getPicResource()),
                    GlobalInformation.getScreenWidth(),
                    GlobalInformation.getScreenHeight(),
                    false
            );
            lastRoom = drawRoom.getID();
        }



        if (drawRoom == null) {
            // room not found
            drawRoom = World.getRoomById(1);
        }

        //Animation für das Subjekt, übernommen aus Subjekt
        /*****************************************************************************************/
        intel.tick();                   //KI aufrufen
        //hier muss später noch ein Aufruf zum ändern der Animation eingefügt werden, z.B. durch die KI oder durch RoomActivity selbst
        switch (animation) {
            case 0:     calculatePosition();
                        break;

            //Weiter Switch-Cases für weitere Animationen

            default:    aktBitmap = subjStandBitmap;        //entweder das oder eine Fehlermeldung
                        break;
        }
        //calculatePosition();

        /*****************************************************************************************/


        // draw the background image
        canvas.drawBitmap(drawRoomB, 0, 0, p);

        // draw all items (TODO: add layering)

        /*
        for (Iterator<Item> iter = World.getAllItems().iterator(); iter.hasNext(); ){
            Item item = iter.next();
            int ID = item.getPicresource();
            for (Iterator<Integer> id = drawRoom.getContainingitems().iterator(); id.hasNext(); ){
                if(id.next() == ID){
                    canvas.drawBitmap(
                            BitmapFactory.decodeResource(resources, item.getPicresource()),
                            item.getXPos().floatValue(),
                            item.getYPos().floatValue(),
                            p);
                }
            }
        }*/

        int i = 0;
        for(Iterator<Integer> it = drawRoom.getContainingitems().iterator(); it.hasNext(); ){
            //Item item = it.next();
            if (World.getItemById(i).getPicresource() == it.next()){
                canvas.drawBitmap(
                        BitmapFactory.decodeResource(resources, it.next()),
                        item.getXPos().floatValue(),
                        item.getYPos().floatValue(),
                        p
                );
            }
            i++;
        }

        /*
        canvas.drawBitmap(
                BitmapFactory.decodeResource(resources, item.getPicresource()),
                item.getXPos().floatValue(),
                item.getYPos().floatValue(),
                p
        );*/


        canvas.drawBitmap(aktBitmap,
                xPos,
                yPos, p);
    }

    private void calculatePosition(){
        if((xPos == xDest) && (aktRoomID == destRoomID)){                   //Animationen basierend auf den Aufrufen
            aktBitmap = subjStandBitmap;
            listPointerWalk = 0;
            SubjectMoveAction nextAction = intel.getNextAction();
            setDest(nextAction.getDestX(), nextAction.getDestRoom());
            // move.getDestItem().use();
        }
        else if (aktRoomID == destRoomID) {
            if (xPos > xDest) {
                swapBitmap(false);
                xPos--;
                xPos--;                                                     //Subjekt schneller
            }
            else{
                swapBitmap(true);
                xPos++;
                xPos++;                                                     //Subjekt schneller
            }
        }
        else if (aktRoomID > destRoomID){
            if (xPos > 0){
                swapBitmap(false);
                xPos--;
                xPos--;                                                     //Subjekt schneller
            }
            else{
                aktBitmap = subjStandBitmap;
                aktRoomID--;
                GlobalInformation.setCurrentRoom(aktRoomID);                        //Kommt später in Subject
                xPos = GlobalInformation.getScreenWidth();
                //reset walking animation
                listPointerWalk = 0;
                sound.startSound(R.raw.sound_door);
            }
        }
        else if (aktRoomID < destRoomID){
            if (xPos < (GlobalInformation.getScreenWidth())){
                swapBitmap(true);
                xPos++;
                xPos++;                                                     //Subjekt schneller
            }
            else{
                aktBitmap = subjStandBitmapInv;
                aktRoomID++;
                GlobalInformation.setCurrentRoom(aktRoomID);                        //Kommt später in Subject
                xPos = 0;
                //reset walking animation
                listPointerWalk = 0;
                sound.startSound(R.raw.sound_door);
            }
        }
    }

    //Methoden aus Subject
    /*****************************************************************************************/
    public void setDest(float x, int room){
        xDest = x;
        destRoomID = room;
    }

    private void swapBitmap(boolean mirror){
        if (mirror == true){
            if (holdAnimation < holdAnimationCycles){
                holdAnimation++;
            }
            else{
                holdAnimation = 0;
                aktBitmap = subjectWalkInv.get(listPointerWalk);
                listPointerWalk++;
                if (listPointerWalk > (subjectWalkInv.size() - 1)){
                    listPointerWalk = 0;
                }
            }
        }
        else
        if (holdAnimation < holdAnimationCycles){
            holdAnimation++;
        }
        else {
            holdAnimation = 0;
            aktBitmap = subjectWalk.get(listPointerWalk);
            listPointerWalk++;
            if (listPointerWalk > (subjectWalk.size() - 1)) {
                listPointerWalk = 0;
            }
        }
    }

    private Bitmap mirrorBitmap(Bitmap b){
        Matrix matrixMirror = new Matrix();
        matrixMirror.preScale(-1.0f, 1.0f);
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrixMirror, false);
        return b;
    }

    /*
    private void startSound(int soundRes){                                                      //Ton abspielen
        Uri path = Uri.parse("android.resource://com.example.Demo_Subj/" + soundRes);
        mediaplayer = new MediaPlayer();
        mediaplayer.reset();
        try{
            mediaplayer.setDataSource(ctx, path);
        }catch (IOException e){
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        try{
            mediaplayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }

        Thread sound = new Thread(new Runnable() {
            @Override
            public void run() {
                mediaplayer.start();
                mediaplayer.setVolume(1.0f, 1.0f);

                AudioManager mAudioManager = (AudioManager)ctx.getSystemService(ctx.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume (AudioManager.STREAM_MUSIC), 0);

                while (mediaplayer.getCurrentPosition() != mediaplayer.getDuration()) {

                }
                mediaplayer.start();
                mediaplayer.setVolume(1,1);
            }
        });
        sound.start();
    }*/
    /*****************************************************************************************/



    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int eventAction = event.getAction();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                // Traverse all Objects in the current room
                for (Iterator<Item> iter = World.getAllItems().iterator(); iter.hasNext(); ){
                    Item item = iter.next();
                    int ID = item.getPicresource();
                    for (Iterator<Integer> id = drawRoom.getContainingitems().iterator(); id.hasNext(); ){
                        if(id.next() == ID){
                            RectF boundingBox = new RectF(item.getXPos().floatValue(),
                                    item.getYPos().floatValue(),
                                    item.getXPos().floatValue() + BitmapFactory.decodeResource(resources, item.getPicresource()).getWidth(),
                                    item.getYPos().floatValue() + BitmapFactory.decodeResource(resources, item.getPicresource()).getHeight());
                            if (boundingBox.contains((int)event.getX(), (int)event.getY())) {
                                // TODO call the use() function of the item
                                Toast.makeText(getContext(), "Click on " + item.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                Rect changeViewBox = new Rect(0,0, 50, 50);
                if (changeViewBox.contains((int)event.getX(), (int)event.getY())) {
                    Intent i = new Intent(ctx, HouseActivity.class);
                    ctx.startActivity(i);
                }
                break;
        }
        return true;
    }
}
