import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.RescaleOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class Apkinstaller extends JFrame {

	private JPanel contentPane;
	String text;
	Vector<String> arrayList=new Vector<>();
	JProgressBar progressBar;
	JTextArea textCommandStatus;
	JButton btnNewButton;
	JButton btnDeviceCheck;
	JList listApk;
	JFileChooser filechooser;
	int p=0;
	int flag;
	
	
	//int num;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Apkinstaller frame = new Apkinstaller();	
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
					frame.setTitle("Apk Batch Installer");
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setIconImage(ImageIO.read(new File("res/icon1.png")));
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	
	public Apkinstaller() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 806, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
        contentPane.setLayout(null);
        
        
        
        
        JLabel lblDragAndDrop = new JLabel("Drag and Drop APK Here");
        lblDragAndDrop.setBounds(10, 14, 248, 14);
        contentPane.add(lblDragAndDrop);
        
        JProgressBar progressBar = new JProgressBar(0,100);
        progressBar.setBounds(10, 315, 770, 14);
        contentPane.add(progressBar);
        
        
        FileDrop drop=new FileDrop( this, new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   for( int i = 0; i < files.length; i++ )
                {   try
                    {  
                        System.out.println(files[i]);
                        arrayList.add(files[i].getCanonicalPath());
                    }   // end try
                    catch( java.io.IOException e ) {}
                }   // end for: through each dropped file
            
            listApk.setListData(arrayList);
            }   // end filesDropped
        }); // end FileDrop.Listener
        
        //Install Button
        btnNewButton = new JButton("Install");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		System.out.println("\n Debug");
        		
        		progressBar.setIndeterminate(false);
        		progressBar.setMinimum(0);
        		progressBar.setMaximum(100);
        		progressBar.setVisible(true);
        		
        		
        		
                // We're going to do something that takes a long time, so we
                // spin off a thread and update the display when we're done.
                Thread worker = new Thread() {
                  public void run() {
                    // Something that takes a long time . . . in real life,
                    // this
                    // might be a DB query, remote method invocation, etc.
                    try {
                    	
                    	progressBar.setIndeterminate(true);
                    	
                    	//Dummy
                	    for (Iterator iterator = arrayList.iterator(); iterator.hasNext();) {
        					String string = (String) iterator.next();
        					System.out.println(string);
        					

        					try {
        						
        						ProcessBuilder pb = new ProcessBuilder("adb", "install","-r",string);
        						Process pc = null;
        						
        						pc = pb.start();
        						
        						
        						InputStream s = pc.getInputStream();
        						
        						BufferedReader in = new BufferedReader(new InputStreamReader(s));
        						
        						String temp;
        						
        						
        						
                                //While 
        		        		while ((temp = in.readLine()) != null) {
        		        			
        		        		    System.out.println(temp);
        		        		    textCommandStatus.append(temp+"\n");;
        						
        						    int exitvalue=pc.waitFor();
        						
        						    System.out.println(exitvalue);
        					        System.out.println("Done");
        					        
        					        				        
        					     }
        		        		//While end
        					   }catch (IOException | InterruptedException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        		        }
                	   flag=1;
                      Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                    }
                    
                    
                       
                    // Report the result using invokeLater().
                    SwingUtilities.invokeLater(new Runnable() {
                      public void run() {
                    	  
                       textCommandStatus.repaint();
                       progressBar.repaint();
                       
                       if(flag==1)
                       {
                    	   progressBar.setIndeterminate(false);
                       }
               
                      }
                    });
                  }
                };

              worker.start(); // So we don't hold up the dispatch thread.
        		
        		
        	}
        });
//DUMMY END		
        		
        	
        
        

        		
        		
        btnNewButton.setBounds(349, 41, 117, 23);
        contentPane.add(btnNewButton);
        
        
        //This shows the status that device is attached or not 
        btnDeviceCheck = new JButton("Device Check");
        btnDeviceCheck.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        			
        		ProcessBuilder pb=new ProcessBuilder("adb","devices");
        		Process p;
				try {
					p =pb.start();
					InputStream s = p.getInputStream();
					
					BufferedReader in = new BufferedReader(new InputStreamReader(s));
					
					String temp;

	        		while ((temp = in.readLine()) != null) {
	        			
	        		    System.out.println(temp);
	        		    textCommandStatus.append(temp+"\n");   
	        		}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				
        	}
        });//Check button end
        
        
        btnDeviceCheck.setBounds(663, 41, 117, 23);
        contentPane.add(btnDeviceCheck);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setViewportBorder(new LineBorder(Color.WHITE));
        scrollPane_1.setToolTipText("CMD VIEW\r\n");
        scrollPane_1.setBounds(349, 92, 431, 184);
        contentPane.add(scrollPane_1);
        
        textCommandStatus = new JTextArea();
        textCommandStatus.setEditable(false);
        scrollPane_1.setViewportView(textCommandStatus);
        textCommandStatus.setForeground(Color.WHITE);
        textCommandStatus.setBackground(Color.BLACK);
        
        JLabel lblCommandLineView = new JLabel("Status");
        lblCommandLineView.setFont(new Font("Arial Unicode MS", Font.PLAIN, 11));
        lblCommandLineView.setBounds(549, 75, 145, 14);
        contentPane.add(lblCommandLineView);
        
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		arrayList.removeAllElements();
        		
        		listApk.repaint();
	
        	}
        });
        btnReset.setBounds(123, 287, 97, 17);
        contentPane.add(btnReset);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 39, 329, 237);
        contentPane.add(scrollPane);
        
        listApk = new JList();
        scrollPane.setViewportView(listApk);
        
        JButton btnRemove = new JButton("-");
        btnRemove.setFont(new Font("Tahoma", Font.ITALIC, 18));
        btnRemove.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		String selected=listApk.getSelectedValue().toString();
        		
        		System.out.println(selected);

        		for (Iterator iterator = arrayList.iterator(); iterator.hasNext();) {
					
        			String string = (String) iterator.next();
					
					if(string.equals(selected))
					{
						iterator.remove();
					}
				}
        		listApk.repaint();
        	}
        	
        });
        btnRemove.setBounds(10, 287, 103, 17);
        contentPane.add(btnRemove);
        
        JButton btnNewButton_1 = new JButton("+");
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		filechooser=new JFileChooser();
        		int returnVal =filechooser.showOpenDialog(null);
        		
        		 if (returnVal == JFileChooser.APPROVE_OPTION) {
        	            File file = filechooser.getSelectedFile();
        	            try {
        	            	System.out.println("\n Debug file adding ");
        	            	System.out.println(file.getCanonicalPath());
        	            	
        	            	arrayList.add(file.getCanonicalPath());

        	            	
        	              
        	            } catch (Exception ex) {
        	              System.out.println("problem accessing file"+file.getAbsolutePath());
        	            }
        	        } 
        	        else {
        	            System.out.println("File access cancelled by user.");
        	        } 
        		 
        	
        		 listApk.setListData(arrayList);
        		 listApk.repaint();

        	}
        });
        btnNewButton_1.setBounds(230, 287, 109, 17);
        contentPane.add(btnNewButton_1);
	}
	
	
}
