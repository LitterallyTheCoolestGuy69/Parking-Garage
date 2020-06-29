import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.Timer;

public class UI extends JPanel implements MouseListener, ActionListener{
	
	private State state = State.overview;
	private Garage garage;
	private int addFloorCount = 1; //Amount of floors to be added
	private int outsideView = 0;  //Which section of garage you are looking at
	private int insideView = 0;  //which section of the floor you are looking at
	private int editFloorNum = 0; //Floor number displayed with button
	private int delFloorNum = 0; //Floor that will be deleted 
	private int parkingTime = 10; //Time that will be allotted to the incoming car
	private int relFloorNum = 0; //Floor of the car being released
	private int relSpaceNum = 0; //Space of the car being released
	private int addSpaceCount = 1; //Amount of spaces to be added
	private int delSpaceNum = 0;   //Space that will be deleted
	private int deactSpaceNum = 0;  //Space that will be activated/deactivated
	private int setSpaceNum = 0;    //Space that will be set manually
	private int setTimeNum = 10;     //Time that will be set manually
	private int viewingFloor = 0;   //Current floor you are viewing
	private int notifType;          //Type of notification to be displayed
	private int enteredSpace = 0;   //The space that the last admitted car was directed to
	private int enteredFloor = 0;   //The floor that the last admitted car was directed to
	private boolean notifViewing = false; //Whether the floor view comes from within a notification
	private int notifType2; //The type of notification to be displayed during floor viewing
	
	private Timer timer;
	
	private ArrayList<Alert> alerts;
	public UI() {
		garage = new Garage();
		alerts = new ArrayList<Alert> ();
		Calendar c = Calendar.getInstance();
		int s = c.get(Calendar.SECOND);
		timer = new Timer((60 - s)*1000, this);
		timer.start();
		//garage.getFloors().get(0).getSpaces().get(0).deactivate();
		garage.getFloors().get(0).getSpaces().get(1).occupy(1);
		state = State.overview;
		
	}
	
	public void update(int x) {
		this.setSize(600,601);
		this.setSize(600,600);
		
		if(x != 0) {
			update(x-1);
		}
	}
	
	public void paint(Graphics g) {
		if(state == State.overview || state == State.outsideNotif) {
			g.setColor(Color.white);
			g.fillRect(0,0,600,600);
			if(outsideView == 0) {
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(0, 500, 600, 100);
			}

		    SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");  
		    SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yy");			    
		    Date d = new Date();
			g.setColor(Color.black);
			g.setFont(new Font("Times Roman", 0, 20));
		    g.drawString(formatter.format(d), 10, 30);
		    g.drawString(formatter2.format(d), 10, 60);
		    
			ArrayList<Floor> fs = garage.getFloors();
			int av;
			
			if(outsideView == 0) {
				if(fs.size() >= 5) {
					for(int i = 0; i < 5; i++) {
						av = 0;
						g.drawRect(150, 500 - (i+1)*(500/6), 200, 500/6);
						g.drawString("Floor " + i, 160, (int)(500 - (i+0.5)*(500/6)) - 10);
						ArrayList<ParkingSpace> f = fs.get(i).getSpaces();
						for(int x = 0; x < f.size(); x++) {
							if(!f.get(x).isOccupied()) {
								av++;
							}
						}
						g.drawString(av + "/" + f.size() + " available", 160, (int)(500 - (i+0.5)*(500/6)) + 10 );
					}
				} else {
					for(int i = 0; i < fs.size(); i++) {
						av = 0;
						g.drawRect(150, 500 - (i+1)*(500/(fs.size()+1)), 200, 500/(fs.size()+1));
						g.drawString("Floor " + i, 160, (int)(500 - (i+0.5)*(500/(fs.size()+1))) - 10);
						ArrayList<ParkingSpace> f = fs.get(i).getSpaces();
						for(int x = 0; x < f.size(); x++) {
							if(!f.get(x).isOccupied() && !f.get(x).isOutOfOrder()) {
								av++;
							}
						}
						g.drawString(av + "/" + f.size() + " available", 160, (int)(500 - (i+0.5)*(500/(fs.size()+1))) + 10 );
					}
				}
			}
			else {
				try {
					for(int i = 0; i < 5; i++) {
						av = 0;
						ArrayList<ParkingSpace> f = fs.get(i + 5*outsideView).getSpaces();
						g.drawRect(150, 500 - ((i+1)*80), 200, 80);
						g.drawString("Floor " + (i + outsideView*5), 160, (int)(500 - ((i+0.5)*80)) - 10);
						for(int x = 0; x < f.size(); x++) {
							if(!f.get(x).isOccupied()  && !f.get(x).isOutOfOrder()) {
								av++;
							}
						}
						g.drawString(av + "/" + f.size() + " available", 160, (int)(500 - (i+0.5)*(80)) + 10 );
					}
				} catch (Exception e) {}
			}
			
			if((fs.size() - 1)/5 > outsideView) {
				g.setColor(Color.gray);
				g.fillRect(200, 10, 100, 50);
				g.setColor(Color.black);
				g.drawRect(200, 10, 100, 50);
				g.drawLine(210, 50, 250, 20);
				g.drawLine(250, 20, 290, 50);
			}
			
			if(outsideView != 0) {
				g.setColor(Color.gray);
				g.fillRect(200, 510, 100, 50);
				g.setColor(Color.black);
				g.drawRect(200, 510, 100, 50);
				g.drawLine(210, 520, 250, 550);
				g.drawLine(250, 550, 290, 520);
			}
			
			
			g.setColor(Color.cyan);
			g.fillRect(400, 5, 150, 60);
			g.setColor(Color.black);
			g.drawRect(400, 5, 150, 60);
			g.drawString("Add Floor", 430, 25);
			
			g.drawRect(405, 30, 30, 30);	
			g.drawRect(515, 30, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 55);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 57);
			g.drawString(""+ addFloorCount, 467, 56);
			
			
			g.setColor(Color.cyan);
			g.fillRect(400, 70, 150, 60);
			g.setColor(Color.black);
			g.drawRect(400, 70, 150, 60);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("View Floor", 430, 90);
			
			g.drawRect(405, 95, 30, 30);	
			g.drawRect(515, 95, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 120);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 122);
			g.drawString("#"+ editFloorNum, 440, 121);
			
			
			g.setColor(Color.cyan);
			g.fillRect(400, 135, 150, 60);
			g.setColor(Color.black);
			g.drawRect(400, 135, 150, 60);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("Delete Floor", 430, 155);   //NTS - Make sure when deleting floor, the edit number cap goes down with the floor count
			
			g.drawRect(405, 160, 30, 30);	
			g.drawRect(515, 160, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 185);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 187);
			g.drawString("#"+ delFloorNum , 440, 186);
				
			
			g.setColor(Color.orange);
			g.fillRect(400, 235, 150, 60);
			g.setColor(Color.black);
			g.drawRect(400, 235, 150, 60);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("Admit Car", 430, 255); 		
			g.drawRect(405, 260, 30, 30);	
			g.drawRect(515, 260, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 285);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 287);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("" + parkingTime + " min", 435, 281);
			
			

			g.setColor(Color.orange);
			g.fillRect(400, 300, 150, 125);
			g.setColor(Color.black);
			g.drawRect(400, 300, 150, 125);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("Release Car", 420, 320);  
			
			g.setFont(new Font("Times Roman", 0, 15));
			g.drawString("Floor", 420, 337);
			
			g.drawRect(405, 342, 30, 30);	
			g.drawRect(515, 342, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 367);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 369);
			g.drawString("#" + relFloorNum, 440, 370);
			
			g.setFont(new Font("Times Roman", 0, 15));
			g.drawString("Space", 420, 385);
			
			g.drawRect(405, 390, 30, 30);	
			g.drawRect(515, 390, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 415);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 417);
			g.drawString("#" + relSpaceNum, 440, 416);
				
			
			g.setColor(Color.black);
			g.setFont(new Font("Times Roman", 0, 15));
			g.drawString("Floors: " + fs.size(), 5, 525);
			
			int totalAv = 0;
			int total = 0;
			ArrayList<ParkingSpace> parkingSpaces;
			for(Floor f: fs) {
				parkingSpaces = f.getSpaces();
				for(ParkingSpace p : parkingSpaces) {
					if(!p.isOccupied() && !p.isOutOfOrder()) {
						totalAv++;
					}
					total++;
				}
			}

			g.drawString("Spaces: " + totalAv + "/" + total, 5, 550);
			
			
			if(state == State.outsideNotif) {
				if(notifType == 0) {
					g.setColor(Color.gray);
					g.fillRect(200, 200, 200, 100);
					g.setColor(Color.black);
					g.drawRect(200, 200, 200, 100);
					g.drawString("Cannot Delete this Floor", 220, 250);
					g.setColor(Color.red);
					g.fillRect(365, 205, 30, 30);
					g.setColor(Color.black);
					g.drawRect(365, 205, 30, 30);
					g.setColor(Color.white);
					g.drawLine(370, 210, 390, 230);
					g.drawLine(370, 230, 390, 210);
				}
				else if(notifType == 1 || notifType == 3 || notifType == 4) {
					g.setColor(Color.gray);
					g.fillRect(200, 200, 200, 140);
					g.setColor(Color.black);
					g.drawRect(200, 200, 200, 140);
					g.drawString("Car may be admitted to", 220, 220);
					g.drawString("space " + enteredSpace + " on floor " + enteredFloor, 220, 240);
					
					g.setColor(Color.cyan);
					g.fillRect(218, 245, 55, 15);
					g.setColor(Color.black);
					g.drawRect(218, 245, 55, 15);
					
					g.setColor(Color.cyan);
					g.fillRect(292, 245, 55, 15);
					g.setColor(Color.black);
					g.drawRect(292, 245, 55, 15);
					g.drawString("change      change", 220, 257);
					
					
					g.setColor(Color.green);
					g.fillRect(205, 265, 30, 30);
					g.setColor(Color.black);
					g.drawRect(205, 265, 30, 30);
					
					

					g.setColor(Color.red);
					g.fillRect(300, 265, 30, 30);
					g.setColor(Color.black);
					g.drawRect(300, 265, 30, 30);
					g.drawString("admit              cancel", 240, 285);
					
					
					g.setColor(new Color(125,0, 255));
					g.fillRect(260, 305, 85, 20);
					g.setColor(Color.black);
					g.drawRect(260, 305, 85, 20);
					g.drawString("view space", 265, 320);
					
					if(notifType == 3) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(225, 220, 50, 100);
						g.setColor(Color.black);
						g.drawRect(225, 220, 50, 100);
						g.drawLine(230, 240, 250, 225);
						g.drawLine(250, 225, 270, 240);
						
						g.drawLine(230, 305, 250, 315);
						g.drawLine(250, 315, 270, 305);
						
						
						g.setColor(Color.red);
						g.fillRect(270, 215, 10, 10);
						g.setColor(Color.white);
						g.drawLine(270, 215, 280, 225);
						g.drawLine(280, 215, 270, 225);
						g.setColor(Color.black);
						g.drawRect(270, 215, 10, 10);
						
						g.setFont(new Font("Times Roman", 0, 25));
						g.drawString("" + enteredSpace, 230, 280);
						
					} else if(notifType == 4) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(300, 220, 50, 100);
						g.setColor(Color.black);
						g.drawRect(300, 220, 50, 100);
						g.drawLine(305, 240, 325, 225);
						g.drawLine(325, 225, 345, 240);
						
						g.drawLine(305, 305, 325, 315);
						g.drawLine(325, 315, 345, 305);
						
						
						g.setColor(Color.red);
						g.fillRect(345, 215, 10, 10);
						g.setColor(Color.white);
						g.drawLine(345, 215, 355, 225);
						g.drawLine(355, 215, 345, 225);
						g.setColor(Color.black);
						g.drawRect(345, 215, 10, 10);
						
						g.setFont(new Font("Times Roman", 0, 25));
						g.drawString("" + enteredFloor, 305, 280);
					}
					
				}
				else if (notifType == 2) {
					g.setColor(Color.gray);
					g.fillRect(200, 200, 200, 100);
					g.setColor(Color.black);
					g.drawRect(200, 200, 200, 100);
					g.drawString("There are no available spaces", 205, 250);
					g.setColor(Color.red);
					g.fillRect(365, 205, 30, 30);
					g.setColor(Color.black);
					g.drawRect(365, 205, 30, 30);
					g.setColor(Color.white);
					g.drawLine(370, 210, 390, 230);
					g.drawLine(370, 230, 390, 210);
				}
				else if(notifType == 5) {
					ParkingSpace PS = garage.getFloors().get(relFloorNum).getSpaces().get(relSpaceNum);
					g.setColor(Color.gray);
					g.fillRect(200, 200, 200, 140);
					g.setColor(Color.black);
					g.drawRect(200, 200, 200, 140);
					g.drawString("     This car was parked in", 205, 220);
					g.drawString("     space " + relSpaceNum + " on floor " + relFloorNum, 205, 240);
					if(PS.getTimeRemaining() < 0) {
						g.drawString("       for " + (PS.getInitTime() - PS.getTimeRemaining()) + " minutes", 205, 260);
						g.drawString("   overstaying by " + (- PS.getTimeRemaining()) + " minutes", 205, 280);
					}
					else
						g.drawString("       for " + (PS.getInitTime() - PS.getTimeRemaining()) + " minutes", 205, 260);
					
					g.setColor(Color.green);
					g.fillRect(205, 305, 30, 30);
					g.setColor(Color.black);
					g.drawRect(205, 305, 30, 30);
					
					g.setColor(Color.red);
					g.fillRect(305, 305, 30, 30);
					g.setColor(Color.black);
					g.drawRect(305, 305, 30, 30);
					
					g.drawString("release              cancel", 240, 320);
				}
				else if(notifType == 6) {
					g.setColor(Color.gray);
					g.fillRect(200, 200, 200, 100);
					g.setColor(Color.black);
					g.drawRect(200, 200, 200, 100);
					g.drawString("No car is parked in that space", 205, 250);
					g.setColor(Color.red);
					g.fillRect(365, 205, 30, 30);
					g.setColor(Color.black);
					g.drawRect(365, 205, 30, 30);
					g.setColor(Color.white);
					g.drawLine(370, 210, 390, 230);
					g.drawLine(370, 230, 390, 210);
				}
			}
		}
		else if(state == State.floorEdit || state == State.insideNotif) {

			g.setColor(Color.white);
			g.fillRect(0,0,600,600);
		    SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");  
		    SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yy");			    
		    Date d = new Date();
			g.setColor(Color.black);
			g.setFont(new Font("Times Roman", 0, 20));
		    g.drawString(formatter.format(d), 10, 30);
		    g.drawString(formatter2.format(d), 10, 60);
		    
		    g.setFont(new Font("Times Roman", 0, 75));
		    g.drawString("Floor #" + viewingFloor, 200, 100);
		    
		    ArrayList<Floor> fs = garage.getFloors();

		    ArrayList<ParkingSpace> psHolder = fs.get(viewingFloor).getSpaces();
	
		    ArrayList<ParkingSpace> ps = new ArrayList<ParkingSpace> ();
		    try {
		    	for(int i = insideView*16; i < insideView*16 + 16; i++) {
		    		ps.add(psHolder.get(i));
		    	}
		    } catch(Exception e) {}
		    
		    
		    int spaces = ps.size();
		    if(insideView == 0 && psHolder.size()<= 16) {
			    if(spaces < 5) {
				    int spacesWidth = (400/(spaces+1));
				    int spacesHeight = (int)(spacesWidth*1.5);
				   
				    for(int i = 0; i < spaces; i++) {
				    	 drawSpace(ps.get(i),((300 - (spaces*(spacesWidth/2))) + (spacesWidth*i)) - 100, 150, spacesWidth, false, (insideView * 16) + i, g);
				    }
			    }
			    else if(spaces < 9) {
			    	int spacesWidth = (400/(4+1));
				    int spacesHeight = (int)(spacesWidth*1.5);
				   
				    for(int i = 0; i < 4; i++) {
				    	 drawSpace(ps.get(i),((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 150, spacesWidth, false, (insideView * 16) +i, g);
				    }
				    for(int i = 0; i < spaces-4; i++) {
				    	drawSpace(ps.get(4+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 300, spacesWidth, true, (insideView * 16) +i + 4, g);
				    }
			    }
			    else if(spaces < 13) {
				    int spacesHeight = 100;
				    int spacesWidth = 66;
				    for(int i = 0; i < 4; i++) {
				    	 drawSpace(ps.get(i),((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 150, spacesWidth, false, (insideView * 16) +i, g);
				    }
				    for(int i = 0; i < 4; i++) {
				    	drawSpace(ps.get(4+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 275, spacesWidth, true, (insideView * 16) +i + 4, g);
				    }
				    for(int i = 0; i < spaces-8; i++) {
				    	drawSpace(ps.get(8+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 400, spacesWidth, false, (insideView * 16) +i + 8, g);
				    }
			    }
			    else {
			    	int spacesHeight = 75;
				    int spacesWidth = 50;
				    for(int i = 0; i < 4; i++) {
				    	 drawSpace(ps.get(i),((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 150, spacesWidth, false, (insideView * 16) +i, g);
				    }
				    for(int i = 0; i < 4; i++) {
				    	drawSpace(ps.get(4+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 250, spacesWidth, true, (insideView * 16) +i + 4, g);
				    }
				    for(int i = 0; i < 4; i++) {
				    	drawSpace(ps.get(8+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 350, spacesWidth, false, (insideView * 16) +i + 8, g);
				    }
				    for(int i = 0; i < spaces-12; i++) {
				    	drawSpace(ps.get(12+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 450, spacesWidth, true, (insideView * 16) +i + 12, g);
				    }
			    }
		    }
		    else
		    {
		    	try {
		    		int spacesHeight = 75;
				    int spacesWidth = 50;
				    for(int i = 0; i < 4; i++) {
				    	 drawSpace(ps.get(i),((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 150, spacesWidth, false, (insideView * 16) +i, g);
				    }
				    for(int i = 0; i < 4; i++) {
				    	drawSpace(ps.get(4+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 250, spacesWidth, true, (insideView * 16) +i + 4, g);
				    }
				    for(int i = 0; i < 4; i++) {
				    	drawSpace(ps.get(8+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 350, spacesWidth, false, (insideView * 16) +i + 8, g);
				    }
				    for(int i = 0; i < spaces-12; i++) {
				    	drawSpace(ps.get(12+i), ((300 - (4*(spacesWidth/2))) + (spacesWidth*i)) - 100, 450, spacesWidth, true, (insideView * 16) +i + 12, g);
				    }
		    	} catch(Exception e) {}
		    }
		    
		    if(psHolder.size() > (insideView+1)*16) {
		    	g.setColor(Color.gray);
		    	g.fillRect(325, 290, 50, 100);
		    	g.setColor(Color.black);
		    	g.drawRect(325, 290, 50, 100);
		    	g.drawLine(335, 300, 365, 340);
		    	g.drawLine(365, 340, 335, 380);
		    }
		    if(insideView != 0) {
		    	g.setColor(Color.gray);
		    	g.fillRect(20, 290, 50, 100);
		    	g.setColor(Color.black);
		    	g.drawRect(20, 290, 50, 100);
		    	g.drawLine(60, 300, 30, 340);
		    	g.drawLine(30, 340, 60, 380);
		    }
		    
		    g.setColor(Color.red);
		    g.fillRect(550, 20, 30, 30);
		    g.setColor(Color.black);
		    g.drawRect(550, 20, 30, 30);
		    g.drawLine(555, 25, 575, 45);
		    g.drawLine(575, 25, 555, 45);
		    
		    
		    
		    
		    g.setColor(Color.cyan);
			g.fillRect(400, 135, 150, 60);
			g.setColor(Color.black);
			g.drawRect(400, 135, 150, 60);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("Add Space", 430, 155);   //NTS - Make sure when deleting floor, the edit number cap goes down with the floor count
			
			g.drawRect(405, 160, 30, 30);	
			g.drawRect(515, 160, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 185);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 187);
			g.drawString("  "+ addSpaceCount , 440, 186);
		    
			g.setColor(Color.cyan);
			g.fillRect(400, 200, 150, 60);
			g.setColor(Color.black);
			g.drawRect(400, 200, 150, 60);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("Remove Space", 410, 220); 		
			g.drawRect(405, 225, 30, 30);	
			g.drawRect(515, 225, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 250);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 252);
			g.setFont(new Font("Times Roman", 0, 30));
			g.drawString("  #" + delSpaceNum, 420, 250);
			
			
			
			g.setColor(Color.cyan);
			g.fillRect(400, 300, 150, 60);
			g.setColor(Color.black);
			g.drawRect(400, 300, 150, 60);
			g.setFont(new Font("Times Roman", 0, 18));
			if(fs.get(viewingFloor).getSpaces().get(deactSpaceNum).isOutOfOrder()) {
				g.drawString("Activate Space", 410, 320);
			}
			else {
				g.drawString("Deactivate Space", 403, 320);
			}
			
			g.setFont(new Font("Times Roman", 0, 15));
			
			g.drawRect(405, 325, 30, 30);	
			g.drawRect(515, 325, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 350);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 352);
			g.setFont(new Font("Times Roman", 0, 30));
			g.drawString("  #" + deactSpaceNum, 420, 351);
			
			
			g.setColor(Color.cyan);
			g.fillRect(400, 365, 150, 125);
			g.setColor(Color.black);
			g.drawRect(400, 365, 150, 125);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("Set Value", 415, 385);  
			
			g.setFont(new Font("Times Roman", 0, 15));
			g.drawString("Space", 420, 402);
			
			g.drawRect(405, 407, 30, 30);	
			g.drawRect(515, 407, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 432);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 434);
			g.setFont(new Font("Times Roman", 0, 30));
			g.drawString("#" + setSpaceNum, 440, 435);
			
			g.setFont(new Font("Times Roman", 0, 15));
			g.drawString("Time Remaining", 420, 450);
			
			g.drawRect(405, 455, 30, 30);	
			g.drawRect(515, 455, 30, 30);
			
			g.setFont(new Font("Times Roman", 0, 40));
			g.drawString("-", 412, 480);
			g.setFont(new Font("Times Roman", 0, 35));
			g.drawString("+", 519, 482);
			g.setFont(new Font("Times Roman", 0, 20));
			g.drawString("" + setTimeNum + " min", 435, 476);
			
			
			if(state == State.insideNotif) {
				if(notifType2 == 0) {
					g.setColor(Color.gray);
					g.fillRect(200, 200, 200, 100);
					g.setColor(Color.black);
					g.drawRect(200, 200, 200, 100);
					g.setFont(new Font("Times Roman", 0, 15));
					g.drawString("Cannot Delete This Space", 205, 250);
					g.setColor(Color.red);
					g.fillRect(365, 205, 30, 30);
					g.setColor(Color.black);
					g.drawRect(365, 205, 30, 30);
					g.setColor(Color.white);
					g.drawLine(370, 210, 390, 230);
					g.drawLine(370, 230, 390, 210);
				}
			}
		}
	}
	
	public void drawSpace(ParkingSpace ps, int x, int y, int w, boolean up, int space, Graphics g) {
		int h = (int)(w*1.5);
		g.setColor(Color.yellow);
		
		g.fillRect(x, y, 5, h);
		g.fillRect(x + w - 5, y, 5, h);
		if(up)
			g.fillRect(x, y+h - 5, w, 5);
		else
			g.fillRect(x, y, w, 5);
		
		
		
		if(ps.isOutOfOrder()) {
			g.setColor(Color.red);
			g.fillPolygon(new int[] {x + 5, x + 10, x + w - 5, x + w - 10}, 
					      new int[] {y + 5, y + 5, y + h - 5, y + h - 5}, 
					      4);

			g.fillPolygon(new int[] {x + 5, x + 10, x + w - 5, x + w - 10}, 
					      new int[] {y + h - 5, y + h - 5, y + 5, y + 5}, 
					      4);
			g.setColor(Color.black);
		}
		else if(ps.isOccupied()) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x + 10, y + 10, w - 20, h - 20);
			
			
			if(ps.getTimeRemaining() < 0) {
				g.setColor(Color.red);
			}
			else {
				g.setColor(Color.black);
			}
		}
		else {
			g.setColor(Color.black);
		}
		
		if(notifViewing && space == enteredSpace) {
			g.setColor(new Color(125,0,255));
		}
		g.setFont(new Font("Times Roman", 0, h / 2));
		if(space >= 10)
			g.drawString(""+ space, x + 2, y + h/2);
		else
			g.drawString("" + space, x + (w / 4), y + h/2);
		
		
		
	}
	
	public void minutePass() {
		alerts = new ArrayList<Alert> ();
		int floors = garage.getFloors().size();
		int position;
		for(int floor = 0; floor < floors; floor++) {
			for(int space = 0; space < garage.getFloors().get(floor).getSpaces().size(); space++) {
				if(!garage.getFloors().get(floor).getSpaces().get(space).passMinute()) {
					if(alerts.size() == 0) {
						alerts.add(new Alert(-garage.getFloors().get(floor).getSpaces().get(space).getTimeRemaining(), "" + floor + "-" + space));
					}
					else {
						position = 0;
						try {
							while(alerts.get(position).getTime() > -garage.getFloors().get(floor).getSpaces().get(space).getTimeRemaining()) {
								position++;
							}
						} catch (Exception e) {}
						alerts.add(position, new Alert(-garage.getFloors().get(floor).getSpaces().get(space).getTimeRemaining(), "" + floor + "-" + space));
					}
				}
			}
		}
		
		System.out.println(alerts);
		update(1);
	}
	

	public void mouseClicked(MouseEvent e) {
		if(state == State.overview) {
			System.out.println(e.getX() + " " + e.getY());
			if(e.getX() > 407 && e.getX() < 557 && e.getY() > 35 && e.getY() < 95) {
				if(e.getX() > 412 && e.getX() < 442 && e.getY() > 60 && e.getY() < 90) {
					if(addFloorCount != 1)
						addFloorCount--;
				}
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 60 && e.getY() < 90) {
					if(addFloorCount != 9)
						addFloorCount++;
				}
				else {
					garage.addFloors(addFloorCount);
				}
				update(1);
			}
			else if(e.getX() > 407 && e.getX() < 557 && e.getY() > 100 && e.getY() < 160) { //View Floor
				if(e.getX() > 412 && e.getX() < 442 && e.getY() > 125 && e.getY() < 155) {
					if(editFloorNum != outsideView*5)
						editFloorNum--;
				}
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 125 && e.getY() < 155) {
					boolean valid = true;
					try {
						garage.getFloors().get(editFloorNum + 1);
					} catch(Exception ex) {
						valid = false;
					}
					if(editFloorNum != ((outsideView + 1)*5 - 1) && valid)
						editFloorNum++;
				}
				else {
					state = State.floorEdit;
					insideView = 0;
					viewingFloor = editFloorNum;
				}
				update(1);
			}
			else if(e.getX() > 407 && e.getX() < 557 && e.getY() > 165 && e.getY() < 225) { //Delete Floor
				if(e.getX() > 412 && e.getX() < 442 && e.getY() > 190 && e.getY() < 220) {
					if(delFloorNum != outsideView*5)
						delFloorNum--;
				}
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 190 && e.getY() < 220) {
					boolean valid = true;
					try {
						garage.getFloors().get(delFloorNum + 1);
					} catch(Exception ex) {
						valid = false;
					}
					if(delFloorNum != ((outsideView + 1)*5 - 1) && valid)
						delFloorNum++;
				}
				else {
					ArrayList<Floor> fs = garage.getFloors();
					ArrayList<ParkingSpace> ps;
					boolean error = false;
					if(fs.size() == 1) {
						error = true;
					}
					else {
						for(int i = delFloorNum; i < fs.size() && (!error); i++) {
							ps = fs.get(i).getSpaces();
							for(ParkingSpace p : ps) {
								if(p.isOccupied()) {
									error = true;
									break;
								}
							}
						}
					}
					
					if(error) {
						state = State.outsideNotif;
						notifType = 0;
					}
					else {
						int a = garage.getFloors().get(delFloorNum).getSpaces().size();
						garage.removeFloor(delFloorNum);
						if(garage.getFloors().size() <= outsideView*5) {
							outsideView--;
						}
						
						if(garage.getFloors().size() == editFloorNum) {
							editFloorNum --;
						}
						if(garage.getFloors().size() == delFloorNum) {
							delFloorNum--;
						}
						
						if(garage.getFloors().size() == relFloorNum) {
							relFloorNum--;
							for(int i = 0; i < a; i++) {
								try {
									garage.getFloors().get(relFloorNum).getSpaces().get(relSpaceNum);
									break;
								} catch(Exception ex) {
									relSpaceNum--;
								}
							}
						}
					}
				}
				update(1);
			}
			else if(e.getX() > 207 && e.getX() < 307 && e.getY() > 40 && e.getY() < 90 && (garage.getFloors().size() - 1)/5 > outsideView) { //Up arrow 
				outsideView++;
				for(int i = 1; i <= 5; i++) {
					try {
						garage.getFloors().get(editFloorNum+1);
						editFloorNum++;
					} catch(Exception ex) {}

					try {
						garage.getFloors().get(delFloorNum+1);
						delFloorNum++;
					} catch(Exception ex) {}
				}
				update(1);
			}
			else if(e.getX() > 207 && e.getX() < 307 && e.getY() > 540 && e.getY() < 590 && outsideView != 0) { //Down arrow 
				outsideView--;
				editFloorNum -= 5;
				delFloorNum -= 5;
				update(1);
			}
			else if(e.getX() > 407 && e.getX() < 557 && e.getY() > 265 && e.getY() < 325) {
				if(e.getX() > 412 && e.getX() < 442 && e.getY() > 290 && e.getY() < 320) {
					if(parkingTime != 5) {
						parkingTime -= 5;
					}
				}
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 290 && e.getY() < 320) {
					parkingTime += 5;
				}
				else {
					ArrayList<Floor> fs = garage.getFloors();
					int f = 0;
					int s = 0;
					boolean found = false;
					for(int floor = 0; floor < fs.size() && !found; floor++) {
						for(int space = 0; space < fs.get(floor).getSpaces().size() && !found; space++) {
							if(!garage.getFloors().get(floor).getSpaces().get(space).isOccupied() &&
							   !garage.getFloors().get(floor).getSpaces().get(space).isOutOfOrder()) {
								f = floor; 
								s = space;
								found = true;
							}
						}
					}
					if(found) {
						notifType = 1;
						state = State.outsideNotif;
						enteredSpace = s;
						enteredFloor = f;
					}
					else {
						notifType = 2;
						state = State.outsideNotif;
					}
				}
				update(1);
			}
			else if(e.getX() > 407 && e.getX() < 557 && e.getY() > 330 && e.getY() < 455) {
				if(e.getX() > 412 && e.getX() < 442 && e.getY() > 372 && e.getY() < 402) {
					if(relFloorNum != 0) {
						relFloorNum--;
						if(relSpaceNum == garage.getFloors().get(relFloorNum).getSpaces().size()) {
							relSpaceNum--;
						}
						update(1);
					}
				}
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 372 && e.getY() < 402) {
					if(relFloorNum < garage.getFloors().size() - 1) {
						relFloorNum++;
						if(relSpaceNum == garage.getFloors().get(relFloorNum).getSpaces().size()) {
							relSpaceNum--;
						}
						update(1);
					}
				}
				else if(e.getX() > 412 && e.getX() < 442 && e.getY() > 420 && e.getY() < 450) {
					if(relSpaceNum != 0) {
						relSpaceNum--;
						update(1);
					}
				}
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 420 && e.getY() < 450) {
					if(relSpaceNum < garage.getFloors().get(relFloorNum).getSpaces().size() - 1) {
						relSpaceNum++;
						update(1);
					}
				}
				else {
					state = State.outsideNotif;
					if(garage.getFloors().get(relFloorNum).getSpaces().get(relSpaceNum).isOccupied())
						notifType = 5;
					else
						notifType = 6;
					update(1);
				}
				
			}
		}
		else if(state == State.outsideNotif) {
			System.out.println(e.getX() + " " + e.getY());
			if(notifType == 0) {
				if(e.getX() > 372 && e.getX() < 402 && e.getY() > 235 && e.getY() < 265) {
					state = State.overview;
					update(1);
				}
			}
			else if(notifType == 1) {
				if(e.getX() > 307 && e.getX() < 337 && e.getY() > 295 && e.getY() < 325) {
					state = State.overview;
					update(1);
				}
				else if(e.getX() > 212 && e.getX() < 242 && e.getY() > 295 && e.getY() < 325) {
					state = State.overview;
					garage.getFloors().get(enteredFloor).getSpaces().get(enteredSpace).occupy(parkingTime);
					update(1);
				}
				else if(e.getX() > 225 && e.getX() < 280 && e.getY() > 275 && e.getY() < 290) {
					notifType = 3;
					update(1);
				}
				else if(e.getX() > 300 && e.getX() < 354 && e.getY() > 275 && e.getY() < 290) {
					notifType = 4;
					update(1);
				}
				else if(e.getX() > 267 && e.getX() < 352 && e.getY() > 335 && e.getY() < 355) {
					state = State.floorEdit;
					viewingFloor = enteredFloor;
					insideView = enteredSpace / 16;
					notifViewing = true;
					update(1);
				}
				
			}
			else if(notifType == 2) {
				if(e.getX() > 372 && e.getX() < 402 && e.getY() > 235 && e.getY() < 265) {
					state = State.overview;
					update(1);
				}
			}
			else if(notifType == 3) {
				if(e.getX() >= 277 && e.getX() <= 287 && e.getY() >= 244 && e.getY() <= 254) {
					notifType = 1;
					update(1);
				}
				else if(e.getX() > 232 && e.getX() < 283 && e.getY() > 250 && e.getY() < 271) {
					try {
						for(int i = 1 ; i < garage.getFloors().get(enteredFloor).getSpaces().size(); i++) {
							if(!garage.getFloors().get(enteredFloor).getSpaces().get(enteredSpace + i).isOccupied() &&
							   !garage.getFloors().get(enteredFloor).getSpaces().get(enteredSpace + i).isOccupied()) {
								enteredSpace += i;
								break;
							}
						}
					} catch(Exception ex) {}
					update(1);
				}
				else if(e.getX() > 232 && e.getX() < 283 && e.getY() > 330 && e.getY() < 350) {
					try {
						for(int i = 1 ; i < garage.getFloors().get(enteredFloor).getSpaces().size(); i++) {
							if(!garage.getFloors().get(enteredFloor).getSpaces().get(enteredSpace - i).isOccupied() &&
							   !garage.getFloors().get(enteredFloor).getSpaces().get(enteredSpace - i).isOccupied()) {
								enteredSpace -= i;
								break;
							}
						}
					} catch(Exception ex) {}
					update(1);
				}
			}
			else if(notifType == 4) {
				if(e.getX() >= 352 && e.getX() <= 362 && e.getY() >= 244 && e.getY() <= 254) {
					notifType = 1;
					update(1);
				}
				else if(e.getX() > 307 && e.getX() < 358 && e.getY() > 250 && e.getY() < 271) {
					try {
						for(int j = 1; j < garage.getFloors().size(); j++) {
							ArrayList<ParkingSpace> ps = garage.getFloors().get(enteredFloor + j).getSpaces();
							boolean valid = false;
							for(int i = 0; i < ps.size(); i++) {
								if(!ps.get(i).isOccupied() && !ps.get(i).isOutOfOrder()){
									valid = true;
								}
							}
							if(valid) {
								enteredFloor += j;
								break;
							}
						}
					} catch(Exception ex) {}
					ArrayList<ParkingSpace> ps = garage.getFloors().get(enteredFloor).getSpaces();
					if(ps.size() <= enteredSpace || ps.get(enteredSpace).isOccupied() || ps.get(enteredSpace).isOutOfOrder()) {
						for(int i = 0; i < ps.size(); i++) {
							if(!ps.get(i).isOccupied() && !ps.get(i).isOutOfOrder()) {
								enteredSpace = i;
								break;
							}
						}
					}
					update(1);
				}
				else if(e.getX() > 307 && e.getX() < 358 && e.getY() > 330 && e.getY() < 350) {
					try {
						for(int j = 1; j < garage.getFloors().size(); j++) {
							ArrayList<ParkingSpace> ps = garage.getFloors().get(enteredFloor - j).getSpaces();
							boolean valid = false;
							for(int i = 0; i < ps.size(); i++) {
								if(!ps.get(i).isOccupied() && !ps.get(i).isOutOfOrder()){
									valid = true;
								}
							}
							if(valid) {
								enteredFloor -= j;
								break;
							}
						}
					} catch(Exception ex) {}
					ArrayList<ParkingSpace> ps = garage.getFloors().get(enteredFloor).getSpaces();
					if(ps.size() <= enteredSpace || ps.get(enteredSpace).isOccupied() || ps.get(enteredSpace).isOutOfOrder()) {
						for(int i = 0; i < ps.size(); i++) {
							if(!ps.get(i).isOccupied() && !ps.get(i).isOutOfOrder()) {
								enteredSpace = i;
								break;
							}
						}
					}
					update(1);
				}
			}
			else if(notifType == 5) {
				if(e.getX() > 212 && e.getX() < 242 && e.getY() > 335 && e.getY() < 365) {
					garage.getFloors().get(relFloorNum).getSpaces().get(relSpaceNum).vacate();
					state = State.overview;
					update(1);
				}
				else if(e.getX() > 312 && e.getX() < 342 && e.getY() > 335 && e.getY() < 365) {
					state = State.overview;
					update(1);
				}
			}
			else if(notifType == 6) {
				if(e.getX() > 372 && e.getX() < 402 && e.getY() > 235 && e.getY() < 265) {
					state = State.overview;
					update(1);
				}
			}
		}
		
		else if(state == State.floorEdit) {
			System.out.println(e.getX() + " " + e.getY());
			if(e.getX() > 557 && e.getX() < 587 && e.getY() > 50 && e.getY() < 80) {
				if(!notifViewing) {
					state = State.overview;
					insideView = 0;
					setTimeNum = 10;
					deactSpaceNum = 0;
					delSpaceNum = 0; 
					addSpaceCount = 1;
					setSpaceNum = 0; 
					update(1);
				}
				else {
					notifViewing = false;
					state = State.outsideNotif;
					update(1);
				}
			}
			else if(e.getX() > 407 && e.getX() < 557 && e.getY() > 165 && e.getY() < 225) {
				if(e.getX() > 412 && e.getX() < 442 && e.getY() > 190 && e.getY() < 220) {
					if(addSpaceCount != 0)
						addSpaceCount--;
				} 
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 190 && e.getY() < 220) {
					if(addSpaceCount != 9)
						addSpaceCount++;
				}
				else if(!notifViewing) {
					garage.getFloors().get(viewingFloor).addSpaces(addSpaceCount);
				}
				update(1);
			}
			else if(e.getX() > 407 && e.getX() < 557 && e.getY() > 230 && e.getY() < 290) {
				if(e.getX() > 412 && e.getX() < 442 && e.getY() > 255 && e.getY() < 285) {
					if(delSpaceNum != insideView*16) {
						delSpaceNum--;
					}
					update(1);
				} 
				else if(e.getX() > 522 && e.getX() < 552 && e.getY() > 255 && e.getY() < 285) {
					if(delSpaceNum != garage.getFloors().get(viewingFloor).getSpaces().size() - 2 && delSpaceNum != ((insideView+1)*16) - 1)
						delSpaceNum++;
					update(1);
				}
				else if(!notifViewing) {
					ArrayList<ParkingSpace> ps = garage.getFloors().get(viewingFloor).getSpaces();
					boolean valid = true;
					for(int i = delSpaceNum; i < ps.size(); i++) {
						if(ps.get(i).isOccupied()) {
							valid = false;
						}
					}
					if(valid) {
						garage.getFloors().get(viewingFloor).removeSpace(delSpaceNum);
						try {
							garage.getFloors().get(viewingFloor).getSpaces().get(delSpaceNum);
						} catch(Exception ex) {
							delSpaceNum--;
						}
						try {
							garage.getFloors().get(viewingFloor).getSpaces().get(setSpaceNum);
						} catch(Exception ex) {
							setSpaceNum--;
						}
						try {
							garage.getFloors().get(viewingFloor).getSpaces().get(deactSpaceNum);
						} catch(Exception ex) {
							deactSpaceNum--;
						}
						
						if(delSpaceNum < (insideView * 16)) {
							insideView--;
						}
					}
					else {
						state = State.insideNotif;
						notifType2 = 0;
						update(1);
					}
				}
				update(1);
			}
			else if(e.getX() > 332 && e.getX() < 382 && e.getY() > 320 && e.getY() < 420) {
				if(garage.getFloors().get(viewingFloor).getSpaces().size() > ((insideView+1)*16) - 1) {
					insideView++;
					try {
						garage.getFloors().get(viewingFloor).getSpaces().get(delSpaceNum + 16);
						delSpaceNum += 16;
					} catch (Exception ex) {
						delSpaceNum = insideView*16;
					}

					try {
						garage.getFloors().get(viewingFloor).getSpaces().get(deactSpaceNum + 16);
						deactSpaceNum += 16;
					} catch (Exception ex) {
						deactSpaceNum = insideView*16;
					}
					
					try {
						garage.getFloors().get(viewingFloor).getSpaces().get(delSpaceNum + 16);
						setSpaceNum += 16;
					} catch (Exception ex) {
						setSpaceNum = insideView*16;
					}
					update(1);
				}
			}
			else if(e.getX() > 27 && e.getX() < 77 && e.getY() > 320 && e.getY() < 420) {
				if(insideView != 0) {
					insideView--;
					delSpaceNum -= 16;
					setSpaceNum -= 16;
					deactSpaceNum -= 16;
					update(1);
				}
			}
		}
		else if(state == State.insideNotif) {
			if(notifType2 == 0) {
				if(e.getX() > 372 && e.getX() < 402 && e.getY() > 235 && e.getY() < 265) {
					state = State.floorEdit;
					update(1);
				}
			}
		}
	}
	
	
	
	
	
	
	
	public enum State{
		overview, floorEdit, outsideNotif, alerts, insideNotif;
	}
	
	
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		timer.stop();
		timer = new Timer(60000, this);
		timer.start();
		minutePass();
		
	}
}
