package code;

import com.esri.mo2.ui.dlg.AboutBox;
import java.util.Vector;
import java.awt.geom.*;
import java.awt.Cursor;
import java.awt.MediaTracker;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.*;
import java.net.URISyntaxException;
import java.util.Iterator;
import javax.swing.*;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import com.esri.mo2.ui.bean.*; // beans used: Map,Layer,Toc,TocAdapter,
        // TocEvent,Legend(a legend is part of a toc)
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.ren.LayerProperties;
import com.esri.mo2.cs.geom.Envelope;
import javax.swing.filechooser.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import com.esri.mo2.data.feat.*; //ShapefileFolder, ShapefileWriter
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.TrueTypeMarkerSymbol;
import com.esri.mo2.map.draw.SimpleMarkerSymbol;
import com.esri.mo2.file.shp.*;
import com.esri.mo2.map.dpy.Layerset;
import com.esri.mo2.map.draw.*;
import com.esri.mo2.ui.bean.Tool;
import java.util.ArrayList;
import com.esri.mo2.cs.geom.BasePointsArray;

public class Earthquake extends JFrame {
double distance;
static boolean helpToolOn;
URL urldraw = getClass().getResource("images/measure_1.gif");
  URL urlhelp2 = getClass().getResource("images/help2.gif");
  URL urlhot = getClass().getResource("images/hotlink.gif");
BasePointsArray bpa = new BasePointsArray();
static AcetateLayer acetLayer;
  static Map map = new Map();
  static boolean fullMap = true;  // Map not zoomed
  Legend legend;
  Legend legend2;
Layer layer0 = new Layer();
  Layer layer = new Layer();
  Layer layer2 = new Layer();
  Layer layer3 = null;
  static com.esri.mo2.map.dpy.Layer layer4;
  JMenuBar mbar = new JMenuBar();
  JMenu file = new JMenu("File");
  JMenu theme = new JMenu("Theme");

JMenu search = new JMenu("Search");
JMenu help = new JMenu("Help");
JMenu helptopics = new JMenu("Help Topics");

JMenuItem tbl = new JMenuItem("Table of Contents",new ImageIcon("images/helptopic.png"));
JMenuItem legendedit = new JMenuItem("Legend Editor",new ImageIcon("images/helptopic.png"));
JMenuItem layercntrl = new JMenuItem("Layer Control",new ImageIcon("images/helptopic.png"));

JMenuItem helptool = new JMenuItem("Help Tool",new ImageIcon("images/help2.gif"));
JMenuItem contactus = new JMenuItem("Contact US",new ImageIcon(""));
JMenuItem abtmojo = new JMenuItem("About MOJO",new ImageIcon(""));
JMenuItem abtpro = new JMenuItem("About Project",new ImageIcon(""));

JMenuItem place = new JMenuItem("Search based on place",new ImageIcon("images/search1.jpg"));
JMenuItem magnitude = new JMenuItem("Search based on magnitude",new ImageIcon("images/search1.jpg"));
JMenuItem clearsel = new JMenuItem("Clear selection",new ImageIcon("images/delete.gif"));

  JMenuItem attribitem = new JMenuItem("open attribute table",
                            new ImageIcon("images/tableview.gif"));
  JMenuItem createlayeritem  = new JMenuItem("create layer from selection",
                    new ImageIcon("images/Icon0915b.jpg"));
  JMenuItem printitem = new JMenuItem("print",new ImageIcon("images/print.gif"));
  JMenuItem addlyritem = new JMenuItem("add layer",new ImageIcon("images/addtheme.gif"));
  JMenuItem remlyritem = new JMenuItem("remove layer",new ImageIcon("images/delete.gif"));
  JMenuItem propsitem = new JMenuItem("Legend Editor",new ImageIcon("images/properties.gif"));
  Toc toc = new Toc();

String s0 = "Data\\World\\world30.shp";
  String s1 = "Data\\World\\Country.shp";
  String s2 = "Data\\QuakeCities\\quakeCities.shp";

  //String s1 = "C:\\ESRI\\MOJ20\\Samples\\Data\\World\\country.shp";
  String datapathname = "";
  String legendname = "";
  ZoomPanToolBar zptb = new ZoomPanToolBar();
  SelectionToolBar stb = new SelectionToolBar();
  JToolBar jtb = new JToolBar();
  ComponentListener complistener;
  JLabel statusLabel = new JLabel("status bar    LOC");
static JLabel milesLabel = new JLabel("   DIST:  0 mi    ");
  static JLabel kmLabel = new JLabel("  0 km    ");
JButton test=new JButton("Coordinates");

com.esri.mo2.map.dpy.Layer activeLayer;
  int activeLayerIndex;

  java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
  JPanel myjp = new JPanel();
  JPanel myjp2 = new JPanel();
  JButton prtjb = new JButton(new ImageIcon("images/print.gif"));
  JButton addlyrjb = new JButton(new ImageIcon("images/addtheme.gif"));
  JButton ptrjb = new JButton(new ImageIcon("images/pointer.gif"));

JButton distjb = new JButton(new ImageIcon("images/measure_1.gif"));
  JButton XYjb = new JButton("XY");
  JButton hotjb = new JButton(new ImageIcon("images/hotlink.gif"));
  JButton helpjb = new JButton(new ImageIcon("images/help2.gif"));


  Arrow arrow = new Arrow();
DistanceTool distanceTool= new DistanceTool();
static HelpTool helpTool = new HelpTool();
ActionListener helplis;
  ActionListener lis;
  ActionListener layerlis;
  TocAdapter mytocadapter;

Toolkit tk = Toolkit.getDefaultToolkit();
Image helper = tk.getImage("images/help3.png"); 
  Image bolt = tk.getImage("images/hotlink_32x32-32.gif");  // 16x16 gif file
  java.awt.Cursor boltCursor = tk.createCustomCursor(bolt,new java.awt.Point(11,26),"bolt");
java.awt.Cursor helpCursor = tk.createCustomCursor(helper, new java.awt.Point(2,2),"helper");
Image dist = tk.getImage("images/airplane.png"); 
java.awt.Cursor distCursor = tk.createCustomCursor(dist, new java.awt.Point(0,0),"dist");

  MyPickAdapter picklis = new MyPickAdapter();
  Identify hotlink = new Identify(); //the Identify class implements a PickListener,
  static String mystate = null;
  class MyPickAdapter implements PickListener {   //implements hotlink
    public void beginPick(PickEvent pe){};
    // this fires even when you click outside the states layer
    public void endPick(PickEvent pe){}
    public void foundData(PickEvent pe){
      //fires only when a layer feature is clicked
      FeatureLayer flayer2 = (FeatureLayer) pe.getLayer();
      com.esri.mo2.data.feat.Cursor c = pe.getCursor();
      Feature f = null;
      System.out.println("inside foundData");
      Fields fields = null;
      if (c != null)
        f = (Feature)c.next();
      fields = f.getFields();
      String sname = fields.getField(2).getName(); //gets col. name for state name
      mystate = (String)f.getValue(2);
      try {
System.out.println("hi");
            HotPick hotpick = new HotPick();//opens dialog window with image in it
            hotpick.setVisible(true);
      } catch(Exception e){}
    }
  };



  static Envelope env;
  public Earthquake() {
    super("Earthquake's");
   this.setBounds(50,50,700,450);
   // this.setSize(950,470);
    zptb.setMap(map);
    stb.setMap(map);
    setJMenuBar(mbar);
helpToolOn=false;
    ActionListener lisZoom = new ActionListener() {
          public void actionPerformed(ActionEvent ae)
	{
            fullMap = false;
	}
	}; // can change a boolean here
        ActionListener lisFullExt = new ActionListener() {
          public void actionPerformed(ActionEvent ae){
            fullMap = true;}};



MouseAdapter mlLisZoom = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(4));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
    MouseAdapter mlLisZoomActive = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	  	HelpDialog helpdialog = new HelpDialog((String)helpText.get(5));
	    helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
	  }
    };
	MouseAdapter mlLisDist = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(6));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
	MouseAdapter mlLisHotlink = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(7));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
    MouseAdapter findLis = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(9));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
    MouseAdapter panOneDiLis = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(10));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
    MouseAdapter panLis = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(11));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
    MouseAdapter zoomOutLis = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(12));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
    MouseAdapter zoomFullExtLis = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(13));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
     MouseAdapter helpJBLis = new MouseAdapter() {
	  public void mousePressed(MouseEvent me) {
	if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
	      try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(13));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
      }
    };
   
        // next line gets ahold of a reference to the zoomin button
        JButton zoomInButton = (JButton)zptb.getActionComponent("ZoomIn");
        JButton zoomFullExtentButton = (JButton)zptb.getActionComponent("ZoomToFullExtent");
        JButton zoomToSelectedLayerButton = (JButton)zptb.getActionComponent("ZoomToSelectedLayer");
        zoomInButton.addActionListener(lisZoom);
	zoomInButton.addMouseListener(mlLisZoom);
        zoomFullExtentButton.addActionListener(lisFullExt);
	zoomToSelectedLayerButton.addMouseListener(mlLisZoomActive);
        zoomToSelectedLayerButton.addActionListener(lisZoom);
	// addlyrjb.addMouseListener(mlLisAddLayer);

	JButton find = (JButton)stb.getActionComponent("Find");
	find.addMouseListener(findLis);
	JButton panOneDi = (JButton)zptb.getActionComponent("PanOneDirection");
	panOneDi.addMouseListener(panOneDiLis);
	JButton pan = (JButton)zptb.getActionComponent("Pan");
	pan.addMouseListener(panLis);
	JButton zoomout = (JButton)zptb.getActionComponent("ZoomOut");
	zoomout.addMouseListener(zoomOutLis);
	zoomFullExtentButton.addMouseListener(zoomFullExtLis);
	
	
        complistener = new ComponentAdapter () {
          public void componentResized(ComponentEvent ce) {
            if(fullMap) {
              map.setExtent(env);
              map.zoom(1.0);    //scale is scale factor in pixels
              map.redraw();
            }
          }
        };
    addComponentListener(complistener);
    lis = new ActionListener() {public void actionPerformed(ActionEvent ae){
          Object source = ae.getSource();
          if (source == prtjb || source instanceof JMenuItem ) {
        com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
        mapPrint.setMap(map);
        mapPrint.doPrint();// prints the map
        }


else if (source == hotjb) {
                hotlink.setCursor(boltCursor);
        map.setSelectedTool(hotlink);
}

else if (source == helpjb) {
	helpToolOn = true;
	helpTool.setCursor(helpCursor);
	map.setSelectedTool(helpTool);
}

      else if (source == ptrjb) {
                map.setSelectedTool(arrow);
	DistanceTool.resetDist();
            }
else if (source == XYjb) {
	try {
	  AddXYtheme addXYtheme = new AddXYtheme();
	  addXYtheme.setMap(map);
	  addXYtheme.setVisible(false);// the file chooser needs a parent
	    // but the parent can stay behind the scenes
	  map.redraw();
	  } catch (IOException e){}
	    }
else if (source == distjb) {
	//DistanceTool distanceTool = new DistanceTool();
	distanceTool.setCursor(distCursor);
	map.setSelectedTool(distanceTool);
        }
          else
            {
                try {
              AddLyrDialog aldlg = new AddLyrDialog();
              aldlg.setMap(map);
              aldlg.setVisible(true);
            } catch(IOException e){}
      }
    }};
    layerlis = new ActionListener() {public void actionPerformed(ActionEvent ae){
          Object source = ae.getSource();
          if (source instanceof JMenuItem) {
                String arg = ae.getActionCommand();
                if(arg == "add layer") {
          try {
                AddLyrDialog aldlg = new AddLyrDialog();
                aldlg.setMap(map);
                aldlg.setVisible(true);
          } catch(IOException e){}
              }
            else if(arg == "remove layer") {
              try {
                        com.esri.mo2.map.dpy.Layer dpylayer =
                           legend.getLayer();
                        map.getLayerset().removeLayer(dpylayer);
                        map.redraw();
                        remlyritem.setEnabled(false);
                        propsitem.setEnabled(false);
                        attribitem.setEnabled(false);
                        stb.setSelectedLayer(null);
                        zptb.setSelectedLayer(null);
              } catch(Exception e) {}
              }
            else if(arg == "Legend Editor") {
          LayerProperties lp = new LayerProperties();
          lp.setLegend(legend);
          lp.setSelectedTabIndex(0);
          lp.setVisible(true);
            }

else if(arg == "Search based on place") {
	SearchPlace searchp=new SearchPlace();
	searchp.setVisible(true);
         
            }
else if(arg == "Search based on magnitude") {
         SearchMagnitude searchm=new SearchMagnitude();
	searchm.setVisible(true);
            }
else if(arg == "Clear selection") {


Iterator itr=map.getAcetateLayers();
	while(itr.hasNext()) {
        Object al = itr.next();
        map.remove((Component) al);
      }
	map.repaint();
	map.redraw();

         
            }

            else if (arg == "open attribute table") {
              try {
                layer4 = legend.getLayer();
            AttrTab attrtab = new AttrTab();
            attrtab.setVisible(true);
              } catch(IOException ioe){}
            }
        else if (arg=="create layer from selection") {
System.out.println("hi+++*********");
int type;
              com.esri.mo2.map.draw.BaseSimpleRenderer sbr = new
                com.esri.mo2.map.draw.BaseSimpleRenderer();                 
	//com.esri.mo2.map.draw.SimplePolygonSymbol simplepolysymbol = new com.esri.mo2.map.draw.SimplePolygonSymbol();// for polygons
               //simplepolysymbol.setPaint(AoFillStyle.getpaint(com.esri.mo2.map.draw.AoFillStyle.SOLID_FILL,new java.awt.Color(255,255,0)));
               //simplepolysymbol.setBoundary(true);


	layer4 = legend.getLayer();
              FeatureLayer flayer2 = (FeatureLayer)layer4;
com.esri.mo2.ui.ren.Util utilObj=new com.esri.mo2.ui.ren.Util();
type=utilObj.getFeatureType(flayer2);
SimpleLineSymbol stSymbol = new SimpleLineSymbol();
SimpleMarkerSymbol smSymbol = new SimpleMarkerSymbol();

com.esri.mo2.map.draw.RasterMarkerSymbol rms=new com.esri.mo2.map.draw.RasterMarkerSymbol();
rms.setSizeX(25);
rms.setSizeY(25);
rms.setImageString("images/bullseye.png");
System.out.println("hi++++");
if(type==2 || type==1)
{
//stSymbol.setTransparency(0.6);
System.out.println("hi");
stSymbol.setLineColor(new Color(0,0,255));
stSymbol.setStroke(AoLineStyle.getStroke(AoLineStyle.DASH_LINE,3));
}
else if(type==0)
{
smSymbol.setAntialiasing(true);
//smSymbol.setTransparency(0.6);
smSymbol.setType(SimpleMarkerSymbol.TRIANGLE_MARKER);
smSymbol.setWidth(10);
smSymbol.setSymbolColor(new Color(0,0,255));
}
              // select, e.g., Montana and then click the
              // create layer menuitem; next line verifies a selection was made
              System.out.println("has selected" + flayer2.hasSelection());
              //next line creates the 'set' of selections
              if (flayer2.hasSelection()) {
	SelectionSet selectset = flayer2.getSelectionSet();
                // next line makes a new feature layer of the selections
                FeatureLayer selectedlayer = flayer2.createSelectionLayer(selectset);
                sbr.setLayer(selectedlayer);
	if(type==2 || type==1)
	{
        sbr.setSymbol(stSymbol);
	}
	else if(type==0)
	{
	//sbr.setSymbol(smSymbol);
	sbr.setSymbol(rms);
	}
                selectedlayer.setRenderer(sbr);
                Layerset layerset = map.getLayerset();
                // next line places a new visible layer, e.g. Montana, on the map
                layerset.addLayer(selectedlayer);
                //selectedlayer.setVisible(true);
                try {
                  legend2 = toc.findLegend(selectedlayer);
                    } catch (Exception e) {}

                    CreateShapeDialog csd = new CreateShapeDialog(selectedlayer);
                    csd.setVisible(true);
                Flash flash = new Flash(legend2);
                flash.start();
                map.redraw(); // necessary to see color immediately

                  }
            }
      }
    }};



helplis = new ActionListener()
                        {public void actionPerformed(ActionEvent ae){
	  Object source = ae.getSource();
	  if (source instanceof JMenuItem) {
	String arg = ae.getActionCommand();
	if(arg == "About MOJO") {
          AboutBox aboutbox = new AboutBox();
          aboutbox.setProductName("MOJO");
          aboutbox.setProductVersion("2.0");
          aboutbox.setVisible(true);
	    }
	    else if(arg == "Contact US") {
	 	ContactUs cu = new ContactUs();
    	cu.setVisible(true);
        }
	    else if(arg == "Table of Contents") {
	  try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(0));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
	    else if(arg == "Legend Editor") {
	  try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(1));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	    }
	    else if(arg == "Layer Control") {
	  try {
	        HelpDialog helpdialog = new HelpDialog((String)helpText.get(2));
            helpdialog.setVisible(true);
          } catch(IOException e){}
	}
	else if(arg == "Help Tool") {
	      try {
            HelpDialog helpdialog = new HelpDialog((String)helpText.get(3));
            helpdialog.setVisible(true);
	      } catch(IOException e){}
	    }
	else if(arg == "About Project") {
	      try {
            HelpDialog helpdialog = new HelpDialog((String)helpText.get(8));
            helpdialog.setVisible(true);
	      } catch(IOException e){}
	    }
	  }
    }};




    toc.setMap(map);
    mytocadapter = new TocAdapter() {
          public void click(TocEvent e) {
            legend = e.getLegend();
        activeLayer = legend.getLayer();
        stb.setSelectedLayer(activeLayer);

	zptb.setSelectedLayer(activeLayer);
        // get active layer index for promote and demote
        activeLayerIndex = map.getLayerset().indexOf(activeLayer);
        // layer indices are in order added, not toc order.
        com.esri.mo2.map.dpy.Layer[] layers = {activeLayer};
        //hotlink.setLayer(activeLayer);// replaces setToc from MOJ10


            remlyritem.setEnabled(true);
            propsitem.setEnabled(true);
            attribitem.setEnabled(true);
             }
    };
    map.addMouseMotionListener(new MouseMotionAdapter() {
          public void mouseMoved(MouseEvent me) {
                com.esri.mo2.cs.geom.Point worldPoint = null;
                if (map.getLayerCount() > 0) {
                  worldPoint = map.transformPixelToWorld(me.getX(),me.getY());
                  String s = "X:"+df.format(worldPoint.getX())+" "+
                             "Y:"+df.format(worldPoint.getY());
                  statusLabel.setText(s);
com.esri.mo2.map.dpy.Layer layer=map.getLayer("quakeCities");
FeatureLayer f2=(FeatureLayer) layer;

//com.esri.mo2.map.dpy.Layer layer2=map.getLayer("capitals");
//FeatureLayer f1=(FeatureLayer) layer2;

//fl.SHOW_TIPS=true;
Fields fields=f2.getFeatureClass().getFields();

int i=fields.findField("CITY_NAME");
Field f=fields.getField(i);
f2.setMapTipField(f);

//int i2=fields.findField("STATE_FIPS");
//Field fi2=fields.getField(i2);
//f1.setMapTipField(fi2);
//System.out.println(map.getMapTipText(me));
              }
            else
              statusLabel.setText("X:0.000 Y:0.000");
      }
	/*public String getMapTipText(MouseEvent event)
	{
	System.out.println("dsa");
	return "dsa";
	}*/
    });

map.setMapTipSensitivity(10.0);
    toc.addTocListener(mytocadapter);
    remlyritem.setEnabled(false); // assume no layer initially selected
    propsitem.setEnabled(false);
    attribitem.setEnabled(false);
    printitem.addActionListener(lis);
    addlyritem.addActionListener(layerlis);
    remlyritem.addActionListener(layerlis);
    propsitem.addActionListener(layerlis);
    attribitem.addActionListener(layerlis);
    createlayeritem.addActionListener(layerlis);

place.addActionListener(layerlis);
magnitude.addActionListener(layerlis);
clearsel.addActionListener(layerlis);

    file.add(addlyritem);
    file.add(printitem);
    file.add(remlyritem);
    file.add(propsitem);
    theme.add(attribitem);
    theme.add(createlayeritem);
search.add(place);
search.add(magnitude);
search.add(clearsel);

help.add(helptopics);
helptopics.add(tbl);
helptopics.add(legendedit);
helptopics.add(layercntrl);
help.add(helptool);
help.add(contactus);
help.add(abtmojo);
help.add(abtpro);

tbl.addActionListener(helplis);
legendedit.addActionListener(helplis);
layercntrl.addActionListener(helplis);
helptool.addActionListener(helplis);
contactus.addActionListener(helplis);
abtmojo.addActionListener(helplis);
abtpro.addActionListener(helplis);

XYjb.addActionListener(lis);
distjb.addActionListener(lis);
helpjb.addActionListener(lis);
XYjb.setToolTipText("add a layer of points from a data file");
distjb.setToolTipText("press-drag-release to measure a distance");
helpjb.setToolTipText("left click here then right click on tool to learn about that tool");
hotjb.addActionListener(lis);
hotjb.setToolTipText("hotlink tool--click somthing to maybe see a picture");
hotlink.addPickListener(picklis);
hotlink.setPickWidth(7);// sets tolerance for hotlink clicks

    mbar.add(file);
    mbar.add(theme);
mbar.add(search);
mbar.add(help);
    prtjb.addActionListener(lis);
    prtjb.setToolTipText("print map");
    addlyrjb.addActionListener(lis);
    addlyrjb.setToolTipText("add layer");
    ptrjb.addActionListener(lis);
    ptrjb.setToolTipText("pointer");
    jtb.add(prtjb);
    jtb.add(addlyrjb);
    jtb.add(ptrjb);
jtb.add(XYjb);
jtb.add(distjb);
jtb.add(hotjb);
jtb.add(helpjb);

    myjp.add(jtb);
    myjp.add(zptb); myjp.add(stb);
    myjp2.add(statusLabel);
myjp2.add(milesLabel);myjp2.add(kmLabel);
setuphelpText();
    getContentPane().add(map, BorderLayout.CENTER);
    getContentPane().add(myjp,BorderLayout.NORTH);
    getContentPane().add(myjp2,BorderLayout.SOUTH);
addShapefileToMap(layer0,s0);
    addShapefileToMap(layer,s1);
    addShapefileToMap(layer2,s2);



 java.util.List list = toc.getAllLegends();
com.esri.mo2.map.dpy.Layer lay2 = ((Legend)list.get(2)).getLayer();
    com.esri.mo2.map.dpy.Layer lay1 = ((Legend)list.get(1)).getLayer();  //states layer
    com.esri.mo2.map.dpy.Layer lay0 = ((Legend)list.get(0)).getLayer();
FeatureLayer flayer2 = (FeatureLayer)lay2;
    FeatureLayer flayer1 = (FeatureLayer)lay1;
    FeatureLayer flayer0 = (FeatureLayer)lay0;
BaseSimpleRenderer bsr2 = (BaseSimpleRenderer)flayer2.getRenderer();
    BaseSimpleRenderer bsr1 = (BaseSimpleRenderer)flayer1.getRenderer();
    BaseSimpleRenderer bsr0 = (BaseSimpleRenderer)flayer0.getRenderer();
SimplePolygonSymbol sym2 = (SimplePolygonSymbol)bsr2.getSymbol();
    SimplePolygonSymbol sym1 = (SimplePolygonSymbol)bsr1.getSymbol();
    SimpleMarkerSymbol sym0 = (SimpleMarkerSymbol)bsr0.getSymbol();
sym2.setPaint(AoFillStyle.getPaint(com.esri.mo2.map.draw.AoFillStyle.SOLID_FILL,new java.awt.Color(113, 166, 255)));
    sym1.setPaint(AoFillStyle.getPaint(com.esri.mo2.map.draw.AoFillStyle.SOLID_FILL,new java.awt.Color(138, 226, 53)));
    //sym0.setSymbolColor(java.awt.Color.red);
sym0.setType(SimpleMarkerSymbol.STAR_MARKER);
sym0.setSymbolColor(new Color(255,0,0));
sym0.setWidth(15);
bsr2.setSymbol(sym2);
    bsr1.setSymbol(sym1);
    bsr0.setSymbol(sym0);




    getContentPane().add(toc, BorderLayout.WEST);
  }
  private void addShapefileToMap(Layer layer,String s) {
    String datapath = s; //"C:\\ESRI\\MOJ10\\Samples\\Data\\USA\\States.shp";
    layer.setDataset("0;"+datapath);
    map.add(layer);
  }



 class ContactUs extends JFrame implements ActionListener {
	public ContactUs() {
	JButton ok = new JButton("OK");
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JLabel centerlabel = new JLabel();
	setBounds(200,100,300,300);
	setTitle("Contact Us");
	ok.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent ae) {
	setVisible(false);
	}});
	String s = "<HTML> <H1>Contact Us:</H1><BR>" +
	"Sree Leela Nadipalli<BR>" +
	"MS Computer Science,<BR>" +
	"San Diego State University<BR>" +
	"5500 Campanile Dr,<BR>" +
	"San Diego, CA 92182<BR>" +
	"USA<BR><BR>" +
	"Email :  sree.leela92@gmail.com<BR>";
	centerlabel.setHorizontalAlignment(JLabel.CENTER);
	centerlabel.setText(s);
	panel1.add(centerlabel);
	panel2.add(ok);
	getContentPane().add(panel2,BorderLayout.SOUTH);
	getContentPane().add(panel1,BorderLayout.CENTER);
	}
	public void actionPerformed(ActionEvent e) {this.setVisible(false);
	}
}
  
  
  private void setuphelpText() {
	String s0 =
	  "    The toc, or table of contents, is to the left of the map. \n" +
	  "    Each entry is called a 'legend' and represents a map 'layer' or \n" +
	  "    'theme'.  If you click on a legend, that layer is called the \n" +
	  "    active layer, or selected layer.  Its display (rendering) properties \n" +
	  "    can be controlled using the Legend Editor, and the legends can be \n" +
	  "    reordered using Layer Control.  Both Legend Editor and Layer Control \n" +
	  "    are separate Help Topics.  This line is e... x... t... e... n... t... e... d"  +
	  "    to test the scrollpane.";
	helpText.add(s0);
	String s1 = "    The Legend Editor is a menu item found under the File menu. \n" +
	  "    Given that a layer is selected by clicking on its legend in the table of \n" +
	  "    contents, clicking on Legend Editor will open a window giving you choices \n" +
	  "    about how to display that layer.  For example you can control the color \n" +
	  "    used to display the layer on the map, or whether to use multiple colors ";
	helpText.add(s1);
	String s2 = " Layer Control is a Menu on the menu bar.  If you have selected a \n"+
	   " layer by clicking on a legend in the toc (table of contents) to the left of \n" +
	   " the map, then the promote and demote tools will become usable.  Clicking on \n" +
	   " promote will raise the selected legend one position higher in the toc, and \n" +
	   " clicking on demote will lower that legend one position in the toc.";
	helpText.add(s2);
	String s3 = "    This tool will allow you to learn about certain other tools. \n" +
	  "    You begin with a standard left mouse button click on the Help Tool itself. \n" +
	  "    RIGHT click on another tool and a window may give you information about the  \n" +
	  "    intended usage of the tool.  Click on the arrow tool to stop using the \n" +
	  "    help tool.";
	helpText.add(s3);
	String s4 = " If you click on the Zoom In tool, and then click on the map, you \n" +
	  " will see a part of the map in greater detail.  You can zoom in multiple times. \n" +
	  " You can also sketch a rectangular part of the map, and zoom to that.  You can \n" +
	  " undo a Zoom In with a Zoom Out or with a Zoom to Full Extent";
	helpText.add(s4);
	String s5 = "    You must have a selected layer to use the Zoom to Active Layer tool.\n" +
	  "    If you then click on Zoom to Active Layer, you will be shown enough of \n" +
	  "    the full map to see all of the features in the layer you select.  If you \n" +
	  "    select a layer that shows where glaciers are, then you do not need to \n" +
	  "    see Hawaii, or any southern states, so you will see Alaska, and northern \n" +
	  "    mainland states.";
	helpText.add(s5);
	String s6 = " This tool will help you to measure distance between two points on the map.\n" +
	  "    If you click on one point and drag to another point you will be able to \n" +
	  "    see the distance between them in miles as well as in km on the bottom panel.";
	helpText.add(s6);
	String s7 = "The hotlink tool is used to click on the points displayed on the map to get\n" +
      "the information about that point on the map. When you click on this icon\n" +
      "the cursor will change to the hotlink symbol that looks like a bolt.\n" +
      "Now click on any one of the points to get the information window.\n";
	helpText.add(s7);
	String s8 = " The objective of this project is to display the top 20 massive earthquakes of 21st\n"+
		    " century. The source of this list is \n"+" http://www.ranker.com/list/the-worst-earthquakes-of-the-century.\n"+
		    " This list has the maximum number of death casued by each earthquake\n"+
		    " along with the maximum magnitude recorded. When we select an event\n"+
		    " from map by mouse click then corresponding data will appear in the window\n"+
		    " along with the picture and learn more link for more information.\n\n"+
		    " 1. Place your cursor on earthquake points. It tells you place of occurrence.\n"+
		    " 2. Click search.\n"+
		    " 3. You can search data based on place, country or magnitude.\n"+
		    " 4. Clear selection allows you to clear all selections on Map.";
	helpText.add(s8);
	String s9 = "    This tool will help you to find earthquakes based on various parameters on the map.\n" +
	  "    Give the required parameters in the given text box. \n" +
	  "    A Layer is created on map based on selection.";
	helpText.add(s9);
	String s10 = "    This tool will help you to pan the map in one direction.\n" +
	  "    Click on this button and select Direction. \n" +
	  "    The map pans in that direction.";
	helpText.add(s10);
	String s11 = "    This tool will help you to pan the map in any direction we want.\n" +
	  "    Click on this button. \n" +
	  "    The map pans in the direction we want.";
	helpText.add(s11);
	String s12 = " If you click on the Zoom Out tool, and then click on the map, you \n" +
	  " will see a part of the map in smaller detail.  You can zoom out multiple times. \n" +
	  " You can also sketch a rectangular part of the map, and zoom to that.  You can \n" +
	  " undo a Zoom Out with a Zoom In or with a Zoom to Full Extent";
	helpText.add(s12);
	String s13 = " If you click on the Zoom to Full Extent tool \n" +
	  " Map gains its original size. This can be used after multiple uses of \n" +
	  " Zoom In and Zoom Out. This regains map with single click. \n";
	helpText.add(s13);
  }

















  public static void main(String[] args) {
    Earthquake qstart = new Earthquake();
    qstart.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            System.out.println("Thanks, Quick Start exits");
            System.exit(0);
        }
    });
    qstart.setVisible(true);
    env = map.getExtent();
  }
private ArrayList helpText = new ArrayList(3);
}
// following is an Add Layer dialog window
class AddLyrDialog extends JDialog {
  Map map;
  ActionListener lis;
  JButton ok = new JButton("OK");
  JButton cancel = new JButton("Cancel");
  JPanel panel1 = new JPanel();
  com.esri.mo2.ui.bean.CustomDatasetEditor cus = new com.esri.mo2.ui.bean.
    CustomDatasetEditor();
  AddLyrDialog() throws IOException {
        setBounds(50,50,520,430);
        setTitle("Select a theme/layer");
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            setVisible(false);
          }
    });
        lis = new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            Object source = ae.getSource();
            if (source == cancel)
              setVisible(false);
            else {
              try {
                        setVisible(false);
                        map.getLayerset().addLayer(cus.getLayer());
                        map.redraw();
                  } catch(IOException e){}
            }
          }
    };
    ok.addActionListener(lis);
    cancel.addActionListener(lis);
    getContentPane().add(cus,BorderLayout.CENTER);
    panel1.add(ok);
    panel1.add(cancel);
    getContentPane().add(panel1,BorderLayout.SOUTH);
  }
  public void setMap(com.esri.mo2.ui.bean.Map map1){
        map = map1;
  }
}

//open csv data file as a layer
class AddXYtheme extends JDialog {
  Map map;
  Vector s2 = new Vector();
  JFileChooser jfc = new JFileChooser();
  BasePointsArray bpa = new BasePointsArray();
  FeatureLayer XYlayer;
  AddXYtheme() throws IOException {
System.out.println("working");
        setBounds(50,50,520,430);
        jfc.showOpenDialog(this);
        try {
          File file  = jfc.getSelectedFile();
          FileReader fred = new FileReader(file);
          BufferedReader in = new BufferedReader(fred);
          String s; // = in.readLine();
          double x,y;
          int n = 0;
          while ((s = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(s,",");
                x = Double.parseDouble(st.nextToken());
                y = Double.parseDouble(st.nextToken());
                bpa.insertPoint(n++,new com.esri.mo2.cs.geom.Point(x,y));
                s2.addElement(st.nextToken());
          }
        } catch (IOException e){}
        XYfeatureLayer xyfl = new XYfeatureLayer(bpa,map,s2);
        XYlayer = xyfl;
        xyfl.setVisible(true);
        map = Earthquake.map;
        map.getLayerset().addLayer(xyfl);
        map.redraw();
       // CreateShapeDialog xydialog = new CreateShapeDialog(XYlayer);
       // xydialog.setVisible(true);
	
  }
  public void setMap(com.esri.mo2.ui.bean.Map map1){
  	map = map1;
  }
}

//select layer and create truetypemarkersymbol
class XYfeatureLayer extends BaseFeatureLayer {
  BaseFields fields;
  private java.util.Vector featureVector;
  public XYfeatureLayer(BasePointsArray bpa,Map map,Vector s2) {
	createFeaturesAndFields(bpa,map,s2);
	BaseFeatureClass bfc = getFeatureClass("MyPoints",bpa);
	setFeatureClass(bfc);
	BaseSimpleRenderer srd = new BaseSimpleRenderer();
    TrueTypeMarkerSymbol ttm = new TrueTypeMarkerSymbol();
	ttm.setFont(new Font("ESRI Enviro Hazard Sites",Font.PLAIN,15));
    ttm.setColor(new Color(255,255,0));
    ttm.setCharacter("101");  //101 house on unsettled land
//SimpleMarkerSymbol sms= new SimpleMarkerSymbol();
//sms.setType(SimpleMarkerSymbol.STAR_MARKER);
        //	sms.setSymbolColor(new Color(0,0,0));
        //	sms.setWidth(15);

	srd.setSymbol(ttm);
	setRenderer(srd);
	XYLayerCapabilities lc = new XYLayerCapabilities();
	setCapabilities(lc);
  }
  private void createFeaturesAndFields(BasePointsArray bpa,Map map,Vector s2) {
	featureVector = new java.util.Vector();
	fields = new BaseFields();
	createDbfFields();
	for(int i=0;i<bpa.size();i++) {
	  BaseFeature feature = new BaseFeature();  //feature is a row
	  feature.setFields(fields);
	  com.esri.mo2.cs.geom.Point p = new
	    com.esri.mo2.cs.geom.Point(bpa.getPoint(i));
	  feature.setValue(0,p);
	  feature.setValue(1,new Integer(0));  // point data
	  feature.setValue(2,(String)s2.elementAt(i));
	  
	  feature.setDataID(new BaseDataID("MyPoints",i));
	  featureVector.addElement(feature);
	}
  }
  private void createDbfFields() {
	fields.addField(new BaseField("#SHAPE#",Field.ESRI_SHAPE,0,0));
	fields.addField(new BaseField("ID",java.sql.Types.INTEGER,9,0));
	fields.addField(new BaseField("Name",java.sql.Types.VARCHAR,200,0));
  }
  public BaseFeatureClass getFeatureClass(String name,BasePointsArray bpa){
    com.esri.mo2.map.mem.MemoryFeatureClass featClass = null;
    try {
	  featClass = new com.esri.mo2.map.mem.MemoryFeatureClass(MapDataset.POINT,
	    fields);
    } catch (IllegalArgumentException iae) {}
    featClass.setName(name);
    for (int i=0;i<bpa.size();i++) {
	  featClass.addFeature((Feature) featureVector.elementAt(i));
    
	}
	
    return featClass;
  }
  private final class XYLayerCapabilities extends
       com.esri.mo2.map.dpy.LayerCapabilities {
    XYLayerCapabilities() {
	  for (int i=0;i<this.size(); i++) {
	setAvailable(this.getCapabilityName(i),true);
	setEnablingAllowed(this.getCapabilityName(i),true);
	getCapability(i).setEnabled(true);
	  }
    }
  }
}

class AttrTab extends JDialog {
  JPanel panel1 = new JPanel();
  com.esri.mo2.map.dpy.Layer layer = Earthquake.layer4;
  JTable jtable = new JTable(new MyTableModel());
  JScrollPane scroll = new JScrollPane(jtable);

  public AttrTab() throws IOException {
          setBounds(70,70,450,350);
          setTitle("Attribute Table");
          addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
              setVisible(false);
            }
    });
    scroll.setHorizontalScrollBarPolicy(
           JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // next line necessary for horiz scrollbar to work
        jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn tc = null;
        int numCols = jtable.getColumnCount();
        //jtable.setPreferredScrollableViewportSize(
                //new java.awt.Dimension(440,340));
        for (int j=0;j<numCols;j++) {
          tc = jtable.getColumnModel().getColumn(j);
          tc.setMinWidth(50);
    }
    getContentPane().add(scroll,BorderLayout.CENTER);
  }
}
class MyTableModel extends AbstractTableModel {
 // the required methods to implement are getRowCount,
 // getColumnCount, getValueAt
  com.esri.mo2.map.dpy.Layer layer = Earthquake.layer4;
  MyTableModel() {
        qfilter.setSubFields(fields);
        com.esri.mo2.data.feat.Cursor cursor = flayer.search(qfilter);
        while (cursor.hasMore()) {
                ArrayList inner = new ArrayList();
                Feature f = (com.esri.mo2.data.feat.Feature)cursor.next();
                inner.add(0,String.valueOf(row));
                for (int j=1;j<fields.getNumFields();j++) {
                  inner.add(f.getValue(j).toString());
	}            data.add(inner);
            row++;
    }
  }
  FeatureLayer flayer = (FeatureLayer) layer;
  FeatureClass fclass = flayer.getFeatureClass();
  String columnNames [] = fclass.getFields().getNames();
  ArrayList data = new ArrayList();
  int row = 0;
  int col = 0;
  BaseQueryFilter qfilter = new BaseQueryFilter();
  Fields fields = fclass.getFields();
  public int getColumnCount() {
        return fclass.getFields().getNumFields();
  }
  public int getRowCount() {
        return data.size();
  }
  public String getColumnName(int colIndx) {
        return columnNames[colIndx];
  }
  public Object getValueAt(int row, int col) {
          ArrayList temp = new ArrayList();
          temp =(ArrayList) data.get(row);
      return temp.get(col);
  }
}
class CreateShapeDialog extends JDialog {

  String name = "";
  String path = "";
  JButton ok = new JButton("OK");
  JButton cancel = new JButton("Cancel");
  JTextField nameField = new JTextField("enter layer name here, then hit ENTER",25);
  com.esri.mo2.map.dpy.FeatureLayer selectedlayer;
  ActionListener lis = new ActionListener() {public void actionPerformed(ActionEvent
ae) {
        Object o = ae.getSource();
        if (o == ok) {
          name = nameField.getText().trim();
        //  path = ((ShapefileFolder)(Earthquake.layer4.getLayerSource())).getPath();
	path="Data";
          System.out.println(path+"    " + name);
com.esri.mo2.ui.ren.Util utilObj=new com.esri.mo2.ui.ren.Util();
int type=utilObj.getFeatureType(selectedlayer);
System.out.println("%%%"+type);
	  try {
		if(type==0)
                	{ShapefileWriter.writeFeatureLayer(selectedlayer,path,name,0);}
		else if(type==1)
                	{ShapefileWriter.writeFeatureLayer(selectedlayer,path,name,1);}
		else if(type==2)
                	{ShapefileWriter.writeFeatureLayer(selectedlayer,path,name,2);}
		else
                	{ShapefileWriter.writeFeatureLayer(selectedlayer,path,name,1);}

          } catch(Exception e) {System.out.println("write error");e.printStackTrace();}
          setVisible(false);
    }
        else if (o == cancel)
      setVisible(false);
        else {
    }
  }};

  JPanel panel1 = new JPanel();
  JLabel centerlabel = new JLabel();
  //centerlabel;
  CreateShapeDialog (com.esri.mo2.map.dpy.FeatureLayer layer5) {
        selectedlayer = layer5;
    setBounds(40,350,450,150);
    setTitle("Create new shapefile?");
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
            setVisible(false);
          }
    });
    nameField.addActionListener(lis);
    ok.addActionListener(lis);
    cancel.addActionListener(lis);
    String s = "<HTML> To make a new shapefile from the new layer, enter<BR>" +
      "the new name you want for the layer and click OK.<BR>" +
      "You can then add it to the map in the usual way.<BR>"+
      "Click ENTER after replacing the text with your layer name";
    centerlabel.setHorizontalAlignment(JLabel.CENTER);
    centerlabel.setText(s);
    getContentPane().add(centerlabel,BorderLayout.CENTER);
    panel1.add(nameField);
    panel1.add(ok);
    panel1.add(cancel);
    getContentPane().add(panel1,BorderLayout.SOUTH);
  }
}
class HelpDialog extends JDialog {
  JTextArea helptextarea;
  public HelpDialog(String inputText) throws IOException {
	setBounds(70,70,460,250);
  	setTitle("Help");
  	addWindowListener(new WindowAdapter() {
  	  public void windowClosing(WindowEvent e) {
  	    setVisible(false);
  	  }
    });
  	helptextarea = new JTextArea(inputText,7,40);
  	JScrollPane scrollpane = new JScrollPane(helptextarea);
    helptextarea.setEditable(false);
    getContentPane().add(scrollpane,"Center");
  }
}
class HelpTool extends Tool {
}
class Arrow extends Tool 
{
	public void arrowChores() { // undo measure tool residue
    Earthquake.milesLabel.setText("DIST   0 mi   ");
    Earthquake.kmLabel.setText("   0 km    ");
    if (Earthquake.acetLayer != null)
      Earthquake.map.remove(Earthquake.acetLayer);
    Earthquake.acetLayer = null;
    Earthquake.map.repaint();
    Earthquake.helpToolOn = false;
  }
	public void mouseClicked(MouseEvent me){}
}
class Flash extends Thread 
{
  	Legend legend;
  	Flash(Legend legendin) 
	{
        	legend = legendin;
  	}
  	public void run() 
	{
        	for (int i=0;i<12;i++) 
	{
	try 
	  	{
                	Thread.sleep(500);
                	legend.toggleSelected();
          	} 
	  	catch (Exception e) {}
    	}
  	}
}
class SearchPlace extends JDialog 
{
	int check=0;
	BasePointsArray bpa = new BasePointsArray();
	Map map=Earthquake.map;
  	String name = "";
  	String path = "";
  	JButton ok = new JButton("OK");
  	JButton cancel = new JButton("Cancel");
  	JTextField nameField = new JTextField("Enter place name!!",25);
  	ActionListener lis = new ActionListener() 
	{
	public void actionPerformed(ActionEvent ae)
	{
        	Object o = ae.getSource();
        	if (o == ok) 
	{
	name=nameField.getText().trim();
          	if(name.equalsIgnoreCase("") || name.equalsIgnoreCase("Enter place name!!"))
	{
	JOptionPane.showMessageDialog(null,"Enter Place Name.");
	}
	else
	{
        	File file  = new File("data/EarthquakeCities.csv");
        	String s; // = in.readLine();
        	        double x,y;
                	int n = 0;
                	try 
	{
              	FileReader fred = new FileReader(file);
             	BufferedReader in = new BufferedReader(fred);
                	  	while ((s = in.readLine()) != null) 
	{
                        	StringTokenizer st = new StringTokenizer(s,",");
                        	x = Double.parseDouble(st.nextToken());
                        	y = Double.parseDouble(st.nextToken());
	String place=st.nextToken().toString();
	String mag=st.nextToken().toString();
	String country=st.nextToken().toString();
	if(place.equalsIgnoreCase(name) || country.equalsIgnoreCase(name))
	{
                        	bpa.insertPoint(n++,new com.esri.mo2.cs.geom.Point(x,y));
	check++;
	}
              	}
            	} 
	catch (IOException e){}
	if(check>0)
	{
	DrawPoint dp=new DrawPoint(bpa,map);
	map.setSelectedTool(dp);
	dp.setVisible(true);
	setVisible(false);
	}
	else
	{
	JOptionPane.showMessageDialog(null,"There is no result for your selection.");	
	}
	}
    	}
        	else if (o == cancel)
      	setVisible(false);
	}
	};
 JPanel panel1 = new JPanel();
 JLabel centerlabel = new JLabel();
 //centerlabel;
 SearchPlace () 
{
    	setBounds(40,350,450,150);
    	setTitle("Search Earthquakes based on place name?");
    	addWindowListener(new WindowAdapter() 
	{
      	public void windowClosing(WindowEvent e) 
	{
        	setVisible(false);
          	}
    	});
    	nameField.addActionListener(lis);
    	ok.addActionListener(lis);
    	cancel.addActionListener(lis);
    	String s = "<HTML>"+"&nbsp  You can highlight certain earthquake by it's place name.<BR>" +
      	"&nbsp  Enter the name below.<BR>" +
      	"&nbsp  Your Selection will be marked.<BR>"+
      	"&nbsp  Happy Searching!!";
    	centerlabel.setHorizontalAlignment(JLabel.CENTER);
    	centerlabel.setText(s);
    	getContentPane().add(centerlabel,BorderLayout.CENTER);
    	panel1.add(nameField);
    	panel1.add(ok);
    	panel1.add(cancel);
    	getContentPane().add(panel1,BorderLayout.SOUTH);
  }
}
class SearchMagnitude extends JDialog 
{
	int check=0;
	BasePointsArray bpa = new BasePointsArray();
	Map map=Earthquake.map;
  	String mvalue = "";
  	String path = "";
  	JButton ok = new JButton("OK");
  	JButton cancel = new JButton("Cancel");
  	JTextField nameField = new JTextField("Enter Magitude value!!",25);
  	ActionListener lis = new ActionListener() 
	{
	public void actionPerformed(ActionEvent ae)
	{
        	Object o = ae.getSource();
        	if (o == ok) 
	{
	mvalue=nameField.getText().trim();
	try
	{
          	if(mvalue.equalsIgnoreCase("") || mvalue.equalsIgnoreCase("Enter Magitude value!!"))
	{
	JOptionPane.showMessageDialog(null,"Enter Magitude value.");
	}
	else
	{
	Double mivalue=Double.parseDouble(mvalue);
	check=0;
        	File file  = new File("data/EarthquakeCities.csv");
        	String s; // = in.readLine();
        	        	double x,y,m;
                	int n = 0;
                	try 
	{
              	FileReader fred = new FileReader(file);
             	BufferedReader in = new BufferedReader(fred);
                	  	while ((s = in.readLine()) != null) 
	{
                        	StringTokenizer st = new StringTokenizer(s,",");
                        	x = Double.parseDouble(st.nextToken());
                        	y = Double.parseDouble(st.nextToken());
	String place=st.nextToken().toString();
	m = Double.parseDouble(st.nextToken());
	if(mivalue==m)
	{
                        	bpa.insertPoint(n++,new com.esri.mo2.cs.geom.Point(x,y));
	check++;
	}
              	}
            	} 
	catch (IOException e){}
	if(check>0)
	{
	DrawPoint dp=new DrawPoint(bpa,map);
	map.setSelectedTool(dp);
	dp.setVisible(true);
	setVisible(false);
	}
	else
	{
	JOptionPane.showMessageDialog(null,"There is no result for your selection.");	
	}
	}
    	}
	catch(Exception e)
	{
	JOptionPane.showMessageDialog(null,"Enter Natural Numbers only.");
	}
	}
        	else if (o == cancel)
      	setVisible(false);
	}
	};
 JPanel panel1 = new JPanel();
 JLabel centerlabel = new JLabel();
 //centerlabel;
 SearchMagnitude () 
{
    	setBounds(40,350,450,150);
    	setTitle("Search Earthquake based on magnitude value?");
    	addWindowListener(new WindowAdapter() 
	{
      	public void windowClosing(WindowEvent e) 
	{
        	setVisible(false);
          	}
    	});
    	nameField.addActionListener(lis);
    	ok.addActionListener(lis);
    	cancel.addActionListener(lis);
    	String s = "<HTML>"+"&nbsp  You can highlight certain earthquake by it's magnitude value.<BR>" +
      	"&nbsp  Enter the value below.<BR>" +
      	"&nbsp  Your Selection will be marked.<BR>"+
      	"&nbsp  Happy Searching!!";
    	centerlabel.setHorizontalAlignment(JLabel.CENTER);
    	centerlabel.setText(s);
    	getContentPane().add(centerlabel,BorderLayout.CENTER);
    	panel1.add(nameField);
    	panel1.add(ok);
    	panel1.add(cancel);
    	getContentPane().add(panel1,BorderLayout.SOUTH);
  }
}
class DrawPoint extends Tool
{
	com.esri.mo2.map.draw.RasterMarkerSymbol rms=new com.esri.mo2.map.draw.RasterMarkerSymbol();
	BasePointsArray bpa;
	Map map;
	AcetateLayer al = new AcetateLayer()
	{
        	public void paintComponent(java.awt.Graphics g) 
	{
	java.awt.Graphics2D g2d=(java.awt.Graphics2D)g;
	g2d.setTransform(map.getWorldToPixelTransform().toAffine());
	g2d.setClip(map.getExtent());
	for(int i=0;i<bpa.size();i++)
	{
	rms.draw(bpa.getPoint(i),g2d,"");
	}
	}   
	};
	public DrawPoint(BasePointsArray bpa1,Map map1)
	{
	bpa=(BasePointsArray)bpa1.clone();
	map=map1;
       	rms.setSizeX(20);
	rms.setSizeY(20);
	rms.setImageString("images/bullseye.png");
	map.addAcetateLayer(al);	
	}
}
//distance tool
/*class DistanceTool extends DragTool  {
  int startx,starty,endx,endy,currx,curry;
  com.esri.mo2.cs.geom.Point initPoint, endPoint, currPoint;
  double distance;
  public static void resetDist() { // undo measure tool residue
    Earthquake.milesLabel.setText("DIST   0 mi   ");
    Earthquake.kmLabel.setText("   0 km    ");
    Earthquake.map.remove(Earthquake.acetLayer);
    Earthquake.acetLayer = null;
    Earthquake.map.repaint();
  }
  public void mousePressed(MouseEvent me) {
	startx = me.getX(); starty = me.getY();
	initPoint = Earthquake.map.transformPixelToWorld(me.getX(),me.getY());
  }
  public void mouseReleased(MouseEvent me) {
	  // now we create an acetatelayer instance and draw a line on it
	endx = me.getX(); endy = me.getY();
	endPoint = Earthquake.map.transformPixelToWorld(me.getX(),me.getY());
    distance = (69.44 / (2*Math.PI)) * 360 * Math.acos(
	 Math.sin(initPoint.y * 2 * Math.PI / 360)
	   * Math.sin(endPoint.y * 2 * Math.PI / 360)
	   + Math.cos(initPoint.y * 2 * Math.PI / 360)
	   * Math.cos(endPoint.y * 2 * Math.PI / 360)
	   * (Math.abs(initPoint.x - endPoint.x) < 180 ?
                    Math.cos((initPoint.x - endPoint.x)*2*Math.PI/360):
                    Math.cos((360 - Math.abs(initPoint.x - endPoint.x))*2*Math.PI/360)));
    System.out.println( distance  );
    Earthquake.milesLabel.setText("DIST: " + new Float((float)distance).toString() + " mi  ");
    Earthquake.kmLabel.setText(new Float((float)(distance*1.6093)).toString() + " km");
    if (Earthquake.acetLayer != null)
      Earthquake.map.remove(Earthquake.acetLayer);
    Earthquake.acetLayer = new AcetateLayer() {
      public void paintComponent(java.awt.Graphics g) {
	java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
	Line2D.Double line = new Line2D.Double(startx,starty,endx,endy);
	g2d.setColor(new Color(0,0,250));
	g2d.draw(line);
      }
    };
    Graphics g = super.getGraphics();
    Earthquake.map.add(Earthquake.acetLayer);
    Earthquake.map.redraw();
  }
  public void cancel() {};
}*/
class DistanceTool extends DragTool  {
  int startx,starty,endx,endy,currx,curry;
  com.esri.mo2.cs.geom.Point initPoint, endPoint, currPoint;
  double distance;
  public static void resetDist() { // undo measure tool residue
    Earthquake.milesLabel.setText("DIST   0 mi   ");
    Earthquake.kmLabel.setText("   0 km    ");
    Earthquake.map.remove(Earthquake.acetLayer);
    Earthquake.acetLayer = null;
    Earthquake.map.repaint();
  }
  public void mousePressed(MouseEvent me) {
        startx = me.getX(); starty = me.getY();
        initPoint = Earthquake.map.transformPixelToWorld(me.getX(),me.getY());
  }
  public void mouseReleased(MouseEvent me) {
          // now we create an acetatelayer instance and draw a line on it
        endx = me.getX(); endy = me.getY();
        endPoint = Earthquake.map.transformPixelToWorld(me.getX(),me.getY());
    distance = (69.44 / (2*Math.PI)) * 360 * Math.acos(
                                 Math.sin(initPoint.y * 2 * Math.PI / 360)
                           * Math.sin(endPoint.y * 2 * Math.PI / 360)
                           + Math.cos(initPoint.y * 2 * Math.PI / 360)
                           * Math.cos(endPoint.y * 2 * Math.PI / 360)
                           * (Math.abs(initPoint.x - endPoint.x) < 180 ?
                    Math.cos((initPoint.x - endPoint.x)*2*Math.PI/360):
                    Math.cos((360 - Math.abs(initPoint.x -
endPoint.x))*2*Math.PI/360)));
    System.out.println( distance  );
    Earthquake.milesLabel.setText("DIST: " + new
Float((float)distance).toString() + " mi  ");
    Earthquake.kmLabel.setText(new Float((float)(distance*1.6093)).toString() + "km");
    if (Earthquake.acetLayer != null)
      Earthquake.map.remove(Earthquake.acetLayer);
    Earthquake.acetLayer = new AcetateLayer() {
      public void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                Line2D.Double line = new Line2D.Double(startx,starty,endx,endy);
                g2d.setColor(new Color(0,0,250));
                g2d.draw(line);
      }
    };
    Graphics g = super.getGraphics();
    Earthquake.map.add(Earthquake.acetLayer);
    Earthquake.map.redraw();
  }
  public void cancel() {};
}
class HotPick extends JDialog {
//URI uri;
  String mystate = Earthquake.mystate;
  String myquake = null;
  String myquakepic = null;
  JPanel jpanel = new JPanel();
  JPanel jpanel2 = new JPanel();
JPanel jpanel3=new JPanel();
String link;
String link2;
  String[][] earthquakes={{"Tohoku","Japan","images/1.jpg","https://en.wikipedia.org/wiki/2011_T%C5%8Dhoku_earthquake_and_tsunami"},
{"Sumatra","Indonesia","images/2.jpg","http://www.ranker.com/list/the-worst-earthquakes-of-the-century/drake-bird"},
{"Illapel","Chile","images/3.jpg", "https://en.wikipedia.org/wiki/2015_Illapel_earthquake"},{"Iquique","Chile","images/4.jpg","https://en.wikipedia.org/wiki/2014_Iquique_earthquake"},
{"Sichuan","Chile","images/5.jpg","https://en.wikipedia.org/wiki/2008_Sichuan_earthquake"},{"Kaikura","New Zealand","images/6.jpg","https://en.wikipedia.org/wiki/2016_Kaikoura_earthquake"},
    {"Muisne","Ecuador","images/7.jpg","https://en.wikipedia.org/wiki/2016_Ecuador_earthquake"},{"Barpak","Nepal","images/8.jpg","https://www.washingtonpost.com/news/worldviews/wp/2015/04/27/the-picturesque-village-of-barpak-was-at-the-epicenter-of-nepals-earthquake-now-its-flattened/?utm_term=.8bcf8c4e8e13"},
    {"Gujarat","India","images/9.jpg","https://en.wikipedia.org/wiki/2001_Gujarat_earthquake"},{"Azad Kashmir","Pakistan","images/10.jpg","https://en.wikipedia.org/wiki/2005_Kashmir_earthquake"},
    {"Jarm","Afghanistan","images/11.jpg","https://en.wikipedia.org/wiki/October_2015_Hindu_Kush_earthquake"},{"Bohol","Phillippines","images/12.jpg","https://en.wikipedia.org/wiki/2013_Bohol_earthquake"},
    {"Honsu","Japan","images/13.jpg","https://www.decodedscience.org/m7-1-earthquake-strikes-honshu-japan-25-october-2013-quake/38606"},{"Ouest","Haiti","images/14.jpg","https://en.wikipedia.org/wiki/2010_Haiti_earthquake"},
    {"Boumerdes","Africa","images/15.jpg","https://en.wikipedia.org/wiki/2003_Boumerd%C3%A8s_earthquake"},{"Bam","Iran","images/16.jpg","https://en.wikipedia.org/wiki/2003_Bam_earthquake"},
    {"Central Italy","Italy","images/17.jpg","https://en.wikipedia.org/wiki/August_2016_Central_Italy_earthquake"},{"Yogyakarta","Indonesia","images/18.jpg","https://en.wikipedia.org/wiki/2006_Yogyakarta_earthquake"},
    {"Kashmir","India","images/19.jpg","https://en.wikipedia.org/wiki/2005_Kashmir_earthquake"},{"Xinjiang","China","images/20.jpg","https://en.wikipedia.org/wiki/2005_Ruichang_earthquake"},
    };
  
HotPick() throws IOException, URISyntaxException {
        setTitle("This was your pick");
    setBounds(250,250,350,350);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
            setVisible(false);
          }
    });
    for (int i = 0;i<51;i++)  {
          if (earthquakes[i][0].equals(mystate)) {
        	myquake = earthquakes[i][1];
            	myquakepic = earthquakes[i][2];
	link="<html><a href='"+earthquakes[i][3]+"' target='_blank'>Learn More</a><html>"; 
	link2=earthquakes[i][3];
	//uri=new URI(link2);
            	break;
          }
    }
    JLabel label = new JLabel(mystate+",   ");
    JLabel label2 = new JLabel(myquake);
    JLabel label3=new JLabel(link);
    ImageIcon birdIcon = new ImageIcon(myquakepic);
    JLabel birdLabel = new JLabel(birdIcon);
    jpanel2.add(birdLabel);
    jpanel.add(label);
    jpanel.add(label2);
jpanel3.add(label3);
label3.setCursor(new Cursor(Cursor.HAND_CURSOR));

label3.addMouseListener(new MouseAdapter() {
       public void mouseEntered(MouseEvent me) {
          label.setCursor(new Cursor(Cursor.HAND_CURSOR));
       }
       public void mouseExited(MouseEvent me) {
          label.setCursor(Cursor.getDefaultCursor());
       }
       public void mouseClicked(MouseEvent me)
       {
          System.out.println("Clicked on Label...");
          try {
	Runtime.getRuntime().exec(new String[] { "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",link2 });
            }
            catch(Exception e) {
		try{
		Desktop.getDesktop().browse(new URI(link2));
               System.out.println(e);}catch(URISyntaxException r){}catch(IOException excep){}
            }
       }
      });



    getContentPane().add(jpanel2,BorderLayout.CENTER);
    getContentPane().add(jpanel,BorderLayout.NORTH);
 getContentPane().add(jpanel3,BorderLayout.SOUTH);
  }
}