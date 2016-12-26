import java.awt.*;
import java.util.Formatter;
import javax.swing.*;
import java.util.Random;


public class BlueRed extends JPanel {
	public class Ball
	{
		 public float ballRadius;
		 public float ballX; // Ball's center (x, y)
		 public float ballY; 
		 public double ballSpeedX;   // Ball's speed for x and y
		 public double ballSpeedY;
		 public float health;
		 public int strenght;
		 public boolean flag;
		 public double min;
		 public Ball enemy;
		 public boolean isDead;
		 public double prevX;
		 public double prevY;
		 Color fading;
		 
		 public Ball(float ballRadius, float ballX, float ballY,double ballSpeedX, double ballSpeedY,int strength, Color c)
		 {
			 this.ballRadius = ballRadius;
			 this.ballX = ballX;
			 this.ballY = ballY;
			 this.ballSpeedX = ballSpeedX;
			 this.ballSpeedY = ballSpeedY;
			 this.health = 100;
			 this.strenght = strenght;             
			 this.flag = false;
			 this.min = 0;
		     this.fading = c;
		     this.prevX = 0;
		     this.prevY = 0;
		 }
		 public double distance(double x, double y){return Math.sqrt(Math.pow((ballX - x),2)+Math.pow((ballY - y),2));}
		 public void addVel(double x, double y){ballX+=x; ballY+=y;}
		 public boolean setEnemy()
		 {
		   if (enemy.isDead)	 
			for (int i = 0; i < blueOnes.length; i++)
			{
				if (!blueOnes[i].isDead)
				{
					enemy = blueOnes[i]; //podstawowa, nieoptymalna wersja
					flag = false;
				}	
			} 
			return false;
		 }
		 public boolean back(double x,double y)
		 {
			 if ((prevX == x) && (prevY == y)) return true;
			 return false;
		 }
         public void harmR(int str)
		 {
			if (enemy.isDead == true) return;
			 if ((str <= 100) && (str >= 90))
			 {
				 health -= 0.15;
				 if(fading.getGreen() < 253)  
				    fading = new Color(fading.getRed(),fading.getGreen()+3,fading.getBlue()+3);
			 }
			 if ((str <= 90) && (str > 80))
			 {
				 health -= 0.10;
				 if(fading.getGreen() < 254)  
				    fading = new Color(fading.getRed(),fading.getGreen()+2,fading.getBlue()+2);
			 }
			 else
			 {
				 health -= 0.05;        //to siê jeszcze dopasuje
				 if(fading.getGreen() < 255) 
				    fading = new Color(fading.getRed(),fading.getGreen()+1,fading.getBlue()+1);
			 }	
			 if (fading.getGreen() == 255)
				 isDead = true;
		 }
		 public void harmB(int str)
		 {
		   if (enemy.isDead == true) return; 
			 if ((str <= 100) && (str >= 90))
			 {
				 health -= 0.15;
				 if(fading.getGreen() < 253)  
				    fading = new Color(fading.getRed()+3,fading.getGreen()+3,fading.getBlue());
			 }
			 if ((str <= 90) && (str > 80))
			 {
				 health -= 0.10;
				 if(fading.getGreen() < 254)  
				    fading = new Color(fading.getRed()+2,fading.getGreen()+2,fading.getBlue());
			 }
			 else
			 {
				 health -= 0.05;        //to siê jeszcze dopasuje
				 if(fading.getGreen() < 255) 
				    fading = new Color(fading.getRed()+1,fading.getGreen()+1,fading.getBlue());
			 }	 
			 if (fading.getGreen() == 255) isDead = true;
		 }
	}
   // Container box's width and height
   private static final int BOX_WIDTH = 640;
   private static final int BOX_HEIGHT = 480;
   private Ball [] redOnes;
   private Ball [] blueOnes;
   private static boolean reservedR[];
   private static boolean reservedB[];
   
   public boolean detected(Ball b) //
   {
  	 for (int i = 0; i < redOnes.length; i++)
  		 if ((b.distance(redOnes[i].ballX, redOnes[i].ballY) < (b.ballRadius + redOnes[i].ballRadius) ) && (redOnes[i] != b) && (redOnes[i].isDead == false) )
  			 return true;
  	 for (int i = 0; i < blueOnes.length; i++)
  		if ((b.distance(blueOnes[i].ballX, blueOnes[i].ballY) < (b.ballRadius + blueOnes[i].ballRadius)) && (blueOnes[i] != b) && (blueOnes[i].isDead == false) )
  			 return true;
  	 return false;
   }
   public boolean detected2(Ball b,double x, double y) //sprawdzanie czy dany punkt nale¿y do kulki
   {
	   for (int i = 0; i < redOnes.length; i++)
	   {
		   if ((redOnes[i].distance(x, y) <= redOnes[i].ballRadius) && (redOnes[i] != b))
			   return true; //logika powinna byæ ok
	   }
       return false;
   }
   public boolean occupied(double x, double y, int i)
   {
	   for(int j = 0; j < i; j++)
		  if(redOnes[j].distance(x,y) <= redOnes[j].ballRadius)
			  return true;
	   return false;
   }
   public boolean checkR(int i)
   {
	   if ((redOnes[i].enemy.ballY > redOnes[i].ballY) && (redOnes[i].enemy.ballX > redOnes[i].ballX) && (detected(redOnes[i]) == false))
		   return true;
	   return false;
   }
   private static final int UPDATE_RATE = 30; // Number of refresh per second
  
   /** Constructor to create the UI components and init game objects. */
   public BlueRed() {      //konstruktor
	  
	  Random r = new Random(); 
	  redOnes = new Ball[70];
	  blueOnes = new Ball[70];
	  
	  reservedR = new boolean[blueOnes.length];
	  reservedB = new boolean[redOnes.length];
	  
	  for(int i = 0; i < redOnes.length; i++)
	  {
		  boolean flag = false;
		  while (!flag){
		  int x = r.nextInt(60)+20;
		  int y = r.nextInt(400)+10;
          if(occupied(x,y,i) == false)
          {
        	 flag = true;
		     redOnes[i] = new Ball(5, x, y,1,0,r.nextInt(30)+70,new Color(255,1,1));
          }
          }
      }	  
	  for(int i = 0; i < blueOnes.length;i++)
		  blueOnes[i] = new Ball(5,r.nextInt(60)+570,r.nextInt(430)+10,-1,0, r.nextInt(30)+70,new Color(1,1,255));
	  
	  for(int i  = 0; i < redOnes.length; i++)   //najbli¿sza niebieska kulka i jej odleg³oœæ
	  {
		  redOnes[i].enemy = blueOnes[0];
		  redOnes[i].min = Math.sqrt(Math.pow((redOnes[i].ballX - blueOnes[0].ballX),2)+Math.pow((redOnes[i].ballY-blueOnes[0].ballY),2));
		  for(int j = 0; j < blueOnes.length; j++)
		  {
			  if (((Math.sqrt(Math.pow((redOnes[i].ballX-blueOnes[j].ballX),2)+Math.pow((redOnes[i].ballY-blueOnes[j].ballY),2)) < redOnes[i].min)) && (reservedR[j] == false))
			  {  
				 reservedR[j] = true;
				 redOnes[i].enemy = blueOnes[j];
			     redOnes[i].min = Math.sqrt(Math.pow((redOnes[i].ballX-blueOnes[j].ballX),2)+Math.pow((redOnes[i].ballY-blueOnes[j].ballY),2));
			  }   
		  }
		 // if (redOnes[i].enemy == null) System.out.print("I have no enemy!");
	  }
	  for(int i  = 0; i < blueOnes.length; i++)   //najbli¿sza niebieska kulka i jej odleg³oœæ
	  {
		  blueOnes[i].enemy = redOnes[0];
		  blueOnes[i].min = Math.sqrt(Math.pow((blueOnes[i].ballX - redOnes[0].ballX),2)+Math.pow((blueOnes[i].ballY-redOnes[0].ballY),2));
		  for(int j = 0; j < redOnes.length; j++)
		  {
			  if (((Math.sqrt(Math.pow((blueOnes[i].ballX-redOnes[j].ballX),2)+Math.pow((blueOnes[i].ballY-redOnes[j].ballY),2)) < blueOnes[i].min)) && (reservedB[j] == false))
			  {  
				 reservedB[j] = true;
				 blueOnes[i].enemy = redOnes[j];
			     blueOnes[i].min = Math.sqrt(Math.pow((blueOnes[i].ballX-redOnes[j].ballX),2)+Math.pow((blueOnes[i].ballY-redOnes[j].ballY),2));
			  }   
		  }
	  }
      this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
      // Start the ball bouncing (in its own thread)
      Thread gameThread = new Thread() {
         public void run() {
            while (true) { // Execute one update step
    		  for(int i = 0; i < redOnes.length;i++)
         	  {
    			  redOnes[i].setEnemy();
    			  for(int j = 0; j < blueOnes.length;j++)
    				  if (redOnes[i].distance(blueOnes[j].ballX,blueOnes[j].ballY) < (redOnes[i].ballRadius+blueOnes[j].ballRadius))
    				  { 
    					   redOnes[i].flag = true;
    					   redOnes[i].harmR(blueOnes[j].strenght);
    				  }
    				  if (redOnes[i].flag == false)
    				  {  
    					 
         		         if ((redOnes[i].enemy.ballY > redOnes[i].ballY) && (redOnes[i].enemy.ballX > redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(redOnes[i].ballSpeedX, 0.25);
         		         else if ((redOnes[i].enemy.ballY < redOnes[i].ballY)&& (redOnes[i].enemy.ballX > redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(redOnes[i].ballSpeedX,-0.25);
         		         else if ((redOnes[i].enemy.ballY < redOnes[i].ballY)&& (redOnes[i].enemy.ballX < redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(-redOnes[i].ballSpeedX, -0.25);
         		         else if ((redOnes[i].enemy.ballY > redOnes[i].ballY)&& (redOnes[i].enemy.ballX < redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(-redOnes[i].ballSpeedX, +0.25);
         		         else if ((redOnes[i].enemy.ballY < redOnes[i].ballY)&& (redOnes[i].enemy.ballX == redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(0, -0.25);
         		         else if ((redOnes[i].enemy.ballY > redOnes[i].ballY) && (redOnes[i].enemy.ballX == redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(0, +0.25);
         		         else if ((redOnes[i].enemy.ballY == redOnes[i].ballY) && (redOnes[i].enemy.ballX > redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(redOnes[i].ballSpeedX, 0);
         		         else if ((redOnes[i].enemy.ballY == redOnes[i].ballY)&& (redOnes[i].enemy.ballX < redOnes[i].ballX) && (detected(redOnes[i]) == false))
         		        	redOnes[i].addVel(-redOnes[i].ballSpeedX, 0);
         		         else
         		         {
         		        	if(detected2(redOnes[i], redOnes[i].ballX + redOnes[i].ballSpeedX, redOnes[i].ballY) == false)
         		        		redOnes[i].addVel(redOnes[i].ballSpeedX, 0);
         		        	else if(detected2(redOnes[i], redOnes[i].ballX, redOnes[i].ballY + 0.25) == false)
         		        		redOnes[i].addVel(0, 0.25);
         		        	else if(detected2(redOnes[i], redOnes[i].ballX, redOnes[i].ballY - 0.25) == false)
         		        		redOnes[i].addVel(0, -0.25);
         		        	else if(detected2(redOnes[i], redOnes[i].ballX - redOnes[i].ballSpeedX, redOnes[i].ballY) == false)
         		        		redOnes[i].addVel(-redOnes[i].ballSpeedX, 0);
         		        	else if(detected2(redOnes[i], redOnes[i].ballX + redOnes[i].ballSpeedX, redOnes[i].ballY + 0.25) == false)
         		        		redOnes[i].addVel(redOnes[i].ballSpeedX, 0.25);
         		        	else if(detected2(redOnes[i], redOnes[i].ballX + redOnes[i].ballSpeedX, redOnes[i].ballY - 0.25) == false)
         		        		redOnes[i].addVel(redOnes[i].ballSpeedX, -0.25);
         		        	else if(detected2(redOnes[i], redOnes[i].ballX - redOnes[i].ballSpeedX, redOnes[i].ballY - 0.25) == false)
         		        		redOnes[i].addVel(-redOnes[i].ballSpeedX, -0.25);
         		        	else if(detected2(redOnes[i], redOnes[i].ballX - redOnes[i].ballSpeedX, redOnes[i].ballY + 0.25) == false)
         		        		redOnes[i].addVel(-redOnes[i].ballSpeedX, +0.25);
         		         }
    				  }
         	  }
               for(int i = 0; i < blueOnes.length;i++){
            	   for(int j = 0; j < redOnes.length;j++)
     				  if (blueOnes[i].distance(redOnes[j].ballX,redOnes[j].ballY) < (redOnes[j].ballRadius+blueOnes[i].ballRadius))
     				  {	
     					  blueOnes[i].flag = true;
     					  blueOnes[i].harmB(redOnes[j].strenght);
     				  }	  
     				  if (blueOnes[i].flag == false) 
            	      {
     				    
     					 if ((blueOnes[i].enemy.ballY > blueOnes[i].ballY) && (detected(blueOnes[i]) == false))
     						blueOnes[i].addVel(blueOnes[i].ballSpeedX, 0.25);
         		         else if ((blueOnes[i].enemy.ballY < blueOnes[i].ballY) && (detected(blueOnes[i]) == false))
         		        	blueOnes[i].addVel(blueOnes[i].ballSpeedX, -0.25);
         		        else
        		         {
        		        	if(detected2(blueOnes[i], blueOnes[i].ballX + blueOnes[i].ballSpeedX, blueOnes[i].ballY) == false)
        		        		blueOnes[i].addVel(blueOnes[i].ballSpeedX, 0);
        		        	else if(detected2(blueOnes[i], blueOnes[i].ballX, blueOnes[i].ballY + 0.25) == false)
        		        		blueOnes[i].addVel(0, 0.25);
        		        	else if(detected2(blueOnes[i], blueOnes[i].ballX, blueOnes[i].ballY - 0.25) == false)
        		        		blueOnes[i].addVel(0, -0.25);
        		        	else if(detected2(blueOnes[i], blueOnes[i].ballX - blueOnes[i].ballSpeedX, blueOnes[i].ballY) == false)
        		        		blueOnes[i].addVel(-blueOnes[i].ballSpeedX, 0);
        		        	else if(detected2(blueOnes[i], blueOnes[i].ballX + blueOnes[i].ballSpeedX, blueOnes[i].ballY+0.25) == false)
        		        		blueOnes[i].addVel(blueOnes[i].ballSpeedX, 0.25);
        		        	else if(detected2(blueOnes[i], blueOnes[i].ballX + blueOnes[i].ballSpeedX, blueOnes[i].ballY - 0.25))
        		        		blueOnes[i].addVel(blueOnes[i].ballSpeedX, -0.25);
        		        	else if(detected2(blueOnes[i], blueOnes[i].ballX - blueOnes[i].ballSpeedX, blueOnes[i].ballY-0.25))
        		        		blueOnes[i].addVel(-blueOnes[i].ballSpeedX, -0.25);
        		        	else if(detected2(blueOnes[i], blueOnes[i].ballX - blueOnes[i].ballSpeedX, blueOnes[i].ballY+0.25))
        		        		blueOnes[i].addVel(-blueOnes[i].ballSpeedX, 0.25);
        		         }
          	          } 
               }
               // Refresh the display
               repaint(); // Callback paintComponent()  //and then we repaint
               // Delay for timing control and give other threads a chance
               try {
                  Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
               } catch (InterruptedException ex) { }
            }
         }
      };
      gameThread.start();  // Callback run()
   }
  
   /** Custom rendering codes for drawing the JPanel */
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);    // Paint background
      
      
      // Draw the box
      g.setColor(Color.BLACK);  
      g.fillRect(0, 0, BOX_WIDTH, BOX_HEIGHT);
  
      for (int i = 0; i < redOnes.length;i++)
      {
    	if (redOnes[i].flag == false)  
    	   g.setColor(Color.RED);
    	else    	
           g.setColor(redOnes[i].fading);
           g.fillOval((int) (redOnes[i].ballX - redOnes[i].ballRadius), (int) (redOnes[i].ballY - redOnes[i].ballRadius),
               (int)(2 * redOnes[i].ballRadius), (int)(2 * redOnes[i].ballRadius));
      }
      for (int i = 0; i < blueOnes.length;i++)
      {
    	if (blueOnes[i].flag == false)  
    	  g.setColor(Color.BLUE);
    	else
           g.setColor(blueOnes[i].fading);        	          
           g.fillOval((int) (blueOnes[i].ballX - blueOnes[i].ballRadius), (int) (blueOnes[i].ballY - blueOnes[i].ballRadius),
               (int)(2 * blueOnes[i].ballRadius), (int)(2 * blueOnes[i].ballRadius)); 
      }
   }
  
   /** main program (entry point) */
   public static void main(String[] args) {
      // Run GUI in the Event Dispatcher Thread (EDT) instead of main thread.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // Set up main window (using Swing's Jframe)
            JFrame frame = new JFrame("A Bouncing Ball");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new BlueRed());
            frame.pack();
            frame.setVisible(true);
         }
      });
   }
}