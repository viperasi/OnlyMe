/*
 * MainFrame.java
 * by viperasi
 * http://www.xu81.com/onlyme.html
 * Created on 2011-10-29, 21:15:50
 */
package com.xu81.onlyme.view;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import com.xu81.onlyme.util.FileHelper;
import com.xu81.onlyme.util.TimeView;

/**
 * 
 * @author xu
 */
public class MainFrame extends javax.swing.JFrame implements ClipboardOwner {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1961766146228671913L;

	private InputStream is;
	private AudioInputStream ais;
	private Clip clip;
	private final static String CHIP_PATH = "/res/01.wav";
	private UndoManager undo = null;
	private Clipboard strBoard = null;
	private Document doc = null;
	
	private JFileChooser jfc = null;
	
	//unsaved file
	private File file = null;
	//file has changed
	private boolean hasChanged = false;
	
	//time viewer
	private TimeView tv = null;
	
	//current page count
	private int pageCount = 1;
	//screen width
	private double screenWidth = 0;
	//screen height
	private double screenHeight = 0;
	//font size
	private int fontSize = 18;
	//rows per page
	private int rowsPerPage = 0;
	
	//define actionmap and actionkey
	private final static String[][] actionMap = {
			{ "Undo"		, 	"control Z"		},
			{ "Redo"		, 	"control Y"		},
			{ "Cut"			, 	"control X"		},
			{ "Copy"		, 	"control C"		},
			{ "Paste"		, 	"control V"		},
			{ "SelectAll"	, 	"control A"		},
			{ "Del"			,	"Delete"		},
			{ "NewFile"		, 	"control N"		},
			{ "OpenFile"	, 	"control O"		},
			{ "SaveFile"	, 	"control S"		},
			{ "SaveAs"		, 	"control D"		},
			{ "Find"		, 	"control F"		},
			{ "FindNext"	, 	"F3"			},
			{ "Replace"		, 	"control R"		},
			{ "Option"		, 	"control P"		},
			{ "About"		, 	"F1"			},
			{ "Quit"		, 	"control Q"		} 
	};

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	//unused
	public static enum Volume {
		MUTE, LOW, MEDIUM, HIGH
	}
	
	public static enum NewOrClose{
		NEW,CLOSE
	}

	//unuserd
	private static Volume volume = Volume.LOW;

	/** Creates new form MainFrame */
	public MainFrame() {
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		initComponents();
		// init sound
		is = MainFrame.class.getResourceAsStream(CHIP_PATH);
		try {
			ais = AudioSystem.getAudioInputStream(is);
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//init undo manager
		undo = new UndoManager();
		
		//get clipboard
		strBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		//get JTextArea document
		doc = taMain.getDocument();
		doc.addUndoableEditListener(new UndoableEditListener() {

			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				doc_addedit(e);
			}
		});
		
		doc.addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				doc_actionListener();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				doc_actionListener();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				doc_actionListener();
			}
		});
		
		//add actionmap for JTextArea
		addActionmap();
		
		tv = new TimeView(lbTime);
		tv.start();
		
		rowsPerPage = (int)screenHeight/fontSize;
		
		jfc = new JFileChooser();
		FileFilter ff = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".txt");
			}

			@Override
			public String getDescription() {
				return "文本文件(*.txt)";
			}
		};
		jfc.setMultiSelectionEnabled(false);
		jfc.setFileFilter(ff);
	}

	private void doc_addedit(UndoableEditEvent e) {
		undo.addEdit(e.getEdit());
	}
	
	private void doc_actionListener(){
		hasChanged = true;
		lbWordCount.setText(String.valueOf(doc.getLength()));
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 * by NetBeans 7.0
	 */
	private void initComponents() {

		jPopupMenu1 = new javax.swing.JPopupMenu();
		miUndo = new javax.swing.JMenuItem();
		miRedo = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JPopupMenu.Separator();
		miCut = new javax.swing.JMenuItem();
		miCopy = new javax.swing.JMenuItem();
		miPaste = new javax.swing.JMenuItem();
		miDel = new javax.swing.JMenuItem();
		jSeparator2 = new javax.swing.JPopupMenu.Separator();
		miSelAll = new javax.swing.JMenuItem();
		jSeparator3 = new javax.swing.JPopupMenu.Separator();
		menuFile = new javax.swing.JMenu();
		miNewFile = new javax.swing.JMenuItem();
		miOpenFIle = new javax.swing.JMenuItem();
		miSaveFile = new javax.swing.JMenuItem();
		miSaveAs = new javax.swing.JMenuItem();
		menuEdit = new javax.swing.JMenu();
		miFind = new javax.swing.JMenuItem();
		miFindNext = new javax.swing.JMenuItem();
		miReplace = new javax.swing.JMenuItem();
		jSeparator5 = new javax.swing.JPopupMenu.Separator();
		miOption = new javax.swing.JMenuItem();
		jSeparator4 = new javax.swing.JPopupMenu.Separator();
		miAbout = new javax.swing.JMenuItem();
		jSeparator6 = new javax.swing.JPopupMenu.Separator();
		miQuit = new javax.swing.JMenuItem();
		jScrollPane1 = new javax.swing.JScrollPane();
		taMain = new javax.swing.JTextArea();
		jPanel1 = new javax.swing.JPanel();
		lbWord = new javax.swing.JLabel();
		lbWordCount = new javax.swing.JLabel();
		lbPage = new javax.swing.JLabel();
		lbPageCount = new javax.swing.JLabel();
		lbChar = new javax.swing.JLabel();
		lbCharCount = new javax.swing.JLabel();
		lbTime = new javax.swing.JLabel();
		lbName = new javax.swing.JLabel();

		jPopupMenu1
				.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
					public void popupMenuCanceled(
							javax.swing.event.PopupMenuEvent evt) {
					}

					public void popupMenuWillBecomeInvisible(
							javax.swing.event.PopupMenuEvent evt) {
					}

					public void popupMenuWillBecomeVisible(
							javax.swing.event.PopupMenuEvent evt) {
						jPopupMenu1PopupMenuWillBecomeVisible(evt);
					}
				});

		miUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Z,
				java.awt.event.InputEvent.CTRL_MASK));
		miUndo.setMnemonic(KeyEvent.VK_U);
		miUndo.setText("撤销(U)");
		miUndo.addActionListener(new MenuActionListener("Undo"));
		jPopupMenu1.add(miUndo);

		miRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Y,
				java.awt.event.InputEvent.CTRL_MASK));
		miRedo.setMnemonic(KeyEvent.VK_R);
		miRedo.setText("恢复(R)");
		miRedo.addActionListener(new MenuActionListener("Redo"));
		jPopupMenu1.add(miRedo);
		jPopupMenu1.add(jSeparator1);

		miCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_X,
				java.awt.event.InputEvent.CTRL_MASK));
		miCut.setMnemonic(KeyEvent.VK_T);
		miCut.setText("剪切(T)");
		miCut.addActionListener(new MenuActionListener("Cut"));
		jPopupMenu1.add(miCut);

		miCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_C,
				java.awt.event.InputEvent.CTRL_MASK));
		miCopy.setMnemonic(KeyEvent.VK_Y);
		miCopy.setText("复制(Y)");
		miCopy.addActionListener(new MenuActionListener("Copy"));
		jPopupMenu1.add(miCopy);

		miPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_V,
				java.awt.event.InputEvent.CTRL_MASK));
		miPaste.setMnemonic(KeyEvent.VK_P);
		miPaste.setText("粘贴(P)");
		miPaste.addActionListener(new MenuActionListener("Paste"));
		jPopupMenu1.add(miPaste);

		miDel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_DELETE, 0));
		miDel.setMnemonic(KeyEvent.VK_D);
		miDel.setText("删除(D)");
		miDel.addActionListener(new MenuActionListener("Del"));
		jPopupMenu1.add(miDel);
		jPopupMenu1.add(jSeparator2);

		miSelAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_A,
				java.awt.event.InputEvent.CTRL_MASK));
		miSelAll.setMnemonic(KeyEvent.VK_A);
		miSelAll.setText("全选(A)");
		miSelAll.addActionListener(new MenuActionListener("SelectAll"));
		jPopupMenu1.add(miSelAll);
		jPopupMenu1.add(jSeparator3);

		menuFile.setMnemonic(KeyEvent.VK_F);
		menuFile.setText("文件(F)");
		menuFile.setActionCommand("File");

		miNewFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N,
				java.awt.event.InputEvent.CTRL_MASK));
		miNewFile.setMnemonic(KeyEvent.VK_N);
		miNewFile.setText("新建(N)");
		miNewFile.addActionListener(new MenuActionListener("NewFile"));
		menuFile.add(miNewFile);

		miOpenFIle.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK));
		miOpenFIle.setMnemonic(KeyEvent.VK_O);
		miOpenFIle.setText("打开(O)");
		miOpenFIle.addActionListener(new MenuActionListener("OpenFile"));
		menuFile.add(miOpenFIle);

		miSaveFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		miSaveFile.setMnemonic(KeyEvent.VK_S);
		miSaveFile.setText("保存(S)");
		miSaveFile.addActionListener(new MenuActionListener("SaveFile"));
		menuFile.add(miSaveFile);

		miSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_D,
				java.awt.event.InputEvent.CTRL_MASK));
		miSaveAs.setMnemonic(KeyEvent.VK_A);
		miSaveAs.setText("另存为(A)");
		miSaveAs.addActionListener(new MenuActionListener("SaveAs"));
		menuFile.add(miSaveAs);

		jPopupMenu1.add(menuFile);

		menuEdit.setMnemonic(KeyEvent.VK_E);
		menuEdit.setText("编辑(E)");

		miFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F,
				java.awt.event.InputEvent.CTRL_MASK));
		miFind.setMnemonic(KeyEvent.VK_F);
		miFind.setText("查找(F)");
		miFind.addActionListener(new MenuActionListener("Find"));
		menuEdit.add(miFind);

		miFindNext.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F3, 0));
		miFindNext.setMnemonic(KeyEvent.VK_X);
		miFindNext.setText("查找下一个(X)");
		miFindNext.addActionListener(new MenuActionListener("FindNext"));
		menuEdit.add(miFindNext);

		miReplace.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_R,
				java.awt.event.InputEvent.CTRL_MASK));
		miReplace.setMnemonic(KeyEvent.VK_E);
		miReplace.setText("替换(E)");
		miReplace.addActionListener(new MenuActionListener("Replace"));
		menuEdit.add(miReplace);

		jPopupMenu1.add(menuEdit);
		jPopupMenu1.add(jSeparator5);

		miOption.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_P,
				java.awt.event.InputEvent.CTRL_MASK));
		miOption.setMnemonic(KeyEvent.VK_P);
		miOption.setText("选项(P)");
		miOption.addActionListener(new MenuActionListener("Option"));
		jPopupMenu1.add(miOption);
		jPopupMenu1.add(jSeparator4);

		miAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F1, 0));
		miAbout.setText("关于");
		miAbout.addActionListener(new MenuActionListener("About"));
		jPopupMenu1.add(miAbout);
		jPopupMenu1.add(jSeparator6);

		miQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Q,
				java.awt.event.InputEvent.CTRL_MASK));
		miQuit.setMnemonic(KeyEvent.VK_Q);
		miQuit.setText("退出(Q)");
		miQuit.addActionListener(new MenuActionListener("Quit"));
		jPopupMenu1.add(miQuit);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		//set system icon
		setIconImage(new javax.swing.ImageIcon(getClass().getResource("/res/onlyme_icon.png")).getImage());
		setTitle("OnlyMe");
		setBackground(new java.awt.Color(0, 0, 0));
		setResizable(false);

		jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));
		jScrollPane1.setBorder(null);
		jScrollPane1.setForeground(new java.awt.Color(255, 153, 0));
		jScrollPane1
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		taMain.setBackground(new java.awt.Color(0, 0, 0));
		taMain.setColumns(20);
		taMain.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
		taMain.setForeground(new java.awt.Color(0, 204, 0));
		taMain.setLineWrap(true);
		taMain.setRows(5);
		taMain.setBorder(null);
		taMain.setComponentPopupMenu(jPopupMenu1);
		taMain.addKeyListener(new java.awt.event.KeyAdapter() {
			
			public void keyPressed(java.awt.event.KeyEvent evt){
				taMainKeyPressed(evt);
			}
		});
		jScrollPane1.setViewportView(taMain);

		jPanel1.setBackground(new java.awt.Color(51, 51, 51));
		jPanel1.setForeground(new java.awt.Color(255, 102, 0));

		lbWord.setFont(new java.awt.Font("宋体", 0, 14));
		lbWord.setForeground(new java.awt.Color(102, 102, 102));
		lbWord.setText("单词:");

		lbWordCount.setFont(new java.awt.Font("宋体", 0, 14));
		lbWordCount.setForeground(new java.awt.Color(102, 102, 102));
		lbWordCount.setText("0");

		lbPage.setFont(new java.awt.Font("宋体", 0, 14));
		lbPage.setForeground(new java.awt.Color(102, 102, 102));
		lbPage.setText("页数:");

		lbPageCount.setFont(new java.awt.Font("宋体", 0, 14));
		lbPageCount.setForeground(new java.awt.Color(102, 102, 102));
		lbPageCount.setText("0");

		lbChar.setFont(new java.awt.Font("宋体", 0, 14));
		lbChar.setForeground(new java.awt.Color(102, 102, 102));
		lbChar.setText("字数:");

		lbCharCount.setFont(new java.awt.Font("宋体", 0, 14));
		lbCharCount.setForeground(new java.awt.Color(102, 102, 102));
		lbCharCount.setText("0");

		lbTime.setFont(new java.awt.Font("宋体", 0, 14));
		lbTime.setForeground(new java.awt.Color(102, 102, 102));
		lbTime.setText("9:30 pm");

		lbName.setFont(new java.awt.Font("宋体", 0, 14));
		lbName.setForeground(new java.awt.Color(102, 102, 102));
		lbName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lbName.setText("*noname");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lbWord)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbWordCount, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbPage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbPageCount, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbChar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbCharCount, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbTime))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lbWordCount, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addComponent(lbPage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbPageCount, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbChar, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbCharCount, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbTime, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(lbWord, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        addWindowListener(new WindowAdapter() {
        	@Override
        	 public void windowClosing(WindowEvent e) {
        		newOrCloseFile(NewOrClose.CLOSE, "退出程序前是否保存当前文件");
        	}
		});
        
		//pack(); comment this code, if not can't use myWindow.setUndecorated(true)  raise ex:The frame is displayable.
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * add actionmap for JTextArea
	 */
	private void addActionmap() {
		ActionMap taAcMap = taMain.getActionMap();
		InputMap taInMap = taMain.getInputMap();

		for (int i = 0; i < actionMap.length; i++) {
			String actionName = actionMap[i][0];
			String actionKey = actionMap[i][1];
			taAcMap.put(actionName, new MenuActionListener(actionName));
			taInMap.put(KeyStroke.getKeyStroke(actionKey), actionName);
		}
	}

	private void taMainKeyPressed(java.awt.event.KeyEvent evt){
		// play sound
		if (volume != Volume.MUTE) {
			if (clip.isRunning()) {
				clip.stop();
			}
			clip.setFramePosition(0);
			clip.start();
		}
	    Rectangle rec = null;
		try {
			//JTextArea current total rows
			rec = taMain.modelToView(taMain.getText().length());
			int rows = rec.y/rec.height + 1;
			if(rows>=rowsPerPage){
				if(rows%rowsPerPage==0){
					pageCount = rows / rowsPerPage;
				} else {
					pageCount = rows / rowsPerPage +  1;
				}
			}
			lbPageCount.setText(String.valueOf(pageCount));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * popupmenu disable/enable detect
	 * @param evt
	 */
	private void jPopupMenu1PopupMenuWillBecomeVisible(
			javax.swing.event.PopupMenuEvent evt) {// GEN-FIRST:event_jPopupMenu1PopupMenuWillBecomeVisible
		miCut.setEnabled(taMain.getSelectedText() != null
				&& !"".equals(taMain.getSelectedText()));
		miCopy.setEnabled(taMain.getSelectedText() != null
				&& !"".equals(taMain.getSelectedText()));
		miDel.setEnabled(taMain.getSelectedText() != null
				&& !"".equals(taMain.getSelectedText()));
		miPaste.setEnabled(strBoard.getContents(this) != null);
		miUndo.setEnabled(undo.canUndo());
		miRedo.setEnabled(undo.canRedo());
	}// GEN-LAST:event_jPopupMenu1PopupMenuWillBecomeVisible

	private javax.swing.JPanel jPanel1;
	private javax.swing.JPopupMenu jPopupMenu1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JPopupMenu.Separator jSeparator1;
	private javax.swing.JPopupMenu.Separator jSeparator2;
	private javax.swing.JPopupMenu.Separator jSeparator3;
	private javax.swing.JPopupMenu.Separator jSeparator4;
	private javax.swing.JPopupMenu.Separator jSeparator5;
	private javax.swing.JPopupMenu.Separator jSeparator6;
	private javax.swing.JLabel lbChar;
	private javax.swing.JLabel lbCharCount;
	private javax.swing.JLabel lbName;
	private javax.swing.JLabel lbPage;
	private javax.swing.JLabel lbPageCount;
	private javax.swing.JLabel lbTime;
	private javax.swing.JLabel lbWord;
	private javax.swing.JLabel lbWordCount;
	private javax.swing.JMenu menuEdit;
	private javax.swing.JMenu menuFile;
	private javax.swing.JMenuItem miAbout;
	private javax.swing.JMenuItem miCopy;
	private javax.swing.JMenuItem miCut;
	private javax.swing.JMenuItem miDel;
	private javax.swing.JMenuItem miFind;
	private javax.swing.JMenuItem miFindNext;
	private javax.swing.JMenuItem miNewFile;
	private javax.swing.JMenuItem miOpenFIle;
	private javax.swing.JMenuItem miOption;
	private javax.swing.JMenuItem miPaste;
	private javax.swing.JMenuItem miQuit;
	private javax.swing.JMenuItem miRedo;
	private javax.swing.JMenuItem miReplace;
	private javax.swing.JMenuItem miSaveAs;
	private javax.swing.JMenuItem miSaveFile;
	private javax.swing.JMenuItem miSelAll;
	private javax.swing.JMenuItem miUndo;
	private javax.swing.JTextArea taMain;

	
	private HelpDialog helpDialog ;
	/**
	 * save current file
	 * @return
	 */
	private boolean saveFile(boolean saveAs){
		if(file==null || saveAs){
			int re = jfc.showSaveDialog(null);
			switch(re){
			case 0:
				File f = jfc.getSelectedFile();
				try {
					file = FileHelper.saveFile(f, taMain.getText(),file==null);
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				break;
			case 1:
			default:
				break;
			}
		}else{
			try {
				file = FileHelper.saveFile(file, taMain.getText(),file==null);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
		lbName.setText(file.getName());
		return file!=null;
	}

	/**
	 * new or close file handler
	 * @param state
	 * @param msg
	 */
	private void newOrCloseFile(NewOrClose state,String msg){
		if(hasChanged){
			int result = JOptionPane.showConfirmDialog(null, msg,"警告",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
			switch(result){
			case 0:
				boolean b = saveFile(false);
				if(b){
					if(state == NewOrClose.NEW){
						taMain.replaceRange("", 0, taMain.getText().length());
						hasChanged = false;
					}else if(state == NewOrClose.CLOSE){
						this.dispose();
						System.exit(0);
					}
				}
				break;
			case 1:
				if(state == NewOrClose.NEW){
					taMain.replaceRange("", 0, taMain.getText().length());
					hasChanged = false;
				}else if(state == NewOrClose.CLOSE){
					this.dispose();
					System.exit(0);
				}
				break;
			case -1:
			default:
				
				break;
			}
		}else{
			if(state == NewOrClose.NEW){
				taMain.replaceRange("", 0, taMain.getText().length());
				hasChanged = false;
			}else if(state == NewOrClose.CLOSE){
				this.dispose();
				System.exit(0);
			}
		}
	}
	
	private void showAboutFrame(boolean isShow){
		if(helpDialog == null){
			helpDialog = new HelpDialog(this, true);
			helpDialog.setBounds((int)(screenWidth-676)/2, (int)(screenHeight-446)/2, 676, 446);
		}
		helpDialog.setVisible(isShow);
	}
	
	/**
	 * MenuItem and JTextArea actionmap action listener
	 * @author xu
	 *
	 */
	class MenuActionListener extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//an is ActionName's short name
		private String an = null;

		public MenuActionListener(String an) {
			this.an = an;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (an != null) {
				if ("Undo".equals(an)) {
					if (undo.canUndo()) {
						undo.undo();
						if(undo.canUndo()){			//if can not undo
							hasChanged = false;		//hasChanged is false
						}
					}
				}else if("Redo".equals(an)){
					if (undo.canRedo()) {
						undo.redo();
					}
				}else if("Cut".equals(an)){
					hasChanged = true;
					String str = taMain.getSelectedText();
					if (str != null && !"".contains(str)) {
						StringSelection sSelection = new StringSelection(str);
						strBoard.setContents(sSelection, MainFrame.this);
						taMain.replaceSelection("");
					}
				}else if("Copy".equals(an)){
					hasChanged = true;
					String str = taMain.getSelectedText();
					if (str != null && !"".contains(str)) {
						StringSelection sSelection = new StringSelection(str);
						strBoard.setContents(sSelection, MainFrame.this);
					}
				}else if("Paste".equals(an)){
					hasChanged = true;
					Transferable sTransf = strBoard.getContents(this);
					if (sTransf != null) {
						try {
							String str = (String) sTransf
									.getTransferData(DataFlavor.stringFlavor);
							taMain.replaceSelection(str);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}else if("SelectAll".equals(an)){
					taMain.selectAll();
				}else if("Del".equals(an)){
					hasChanged = true;
					taMain.replaceSelection("");
				}else if("NewFile".equals(an)){
					newOrCloseFile(NewOrClose.NEW,"新建文件前是否保存当前文件?");
				}else if("OpenFile".equals(an)){
					int result = jfc.showOpenDialog(null);
					if(result==0){
						File f = jfc.getSelectedFile();
						if(f!=null){
							file = f;
						}
						try {
							String content = FileHelper.openFile(f);
							taMain.setText(content);
							hasChanged = false;
						} catch (IOException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
						lbName.setText(file.getName());
					}
				}else if("SaveFile".equals(an)){
					hasChanged = !saveFile(false);
				}else if("SaveAs".equals(an)){
					hasChanged = !saveFile(true);
				}else if("Find".equals(an)){
					
				}else if("FindNext".equals(an)){
					
				}else if("Replace".equals(an)){
					
				}else if("Option".equals(an)){
					
				}else if("About".equals(an)){
					showAboutFrame(true);
				}else if("Quit".equals(an)){
					MainFrame.this.processWindowEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
				}
			}
		}

	}
}
