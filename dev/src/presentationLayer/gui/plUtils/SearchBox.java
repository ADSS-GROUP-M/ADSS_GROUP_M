package presentationLayer.gui.plUtils;


import presentationLayer.gui.plAbstracts.Searchable;
import presentationLayer.gui.plAbstracts.Panel;
import presentationLayer.gui.plAbstracts.TransportBasePanel;
import presentationLayer.gui.plAbstracts.UIElement;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

import static presentationLayer.gui.plUtils.Fonts.textBoxFont;


public class SearchBox implements UIElement {

    private final List<Searchable> data;
    private final String defaultText;
    private final Dimension size;
    private final JComboBox<String> comboBox;
    private boolean clearTextOnClick;
    private boolean isPopupClickable;
    private String[] lastFilteredData;
    private String text;

    public SearchBox(List<Searchable> data,String defaultText, Dimension size, PopupMenuListener repaintListener){
        this.size = size;
        this.data = data;
        String[] dataStrings = data.stream()
                .map(Searchable::getShortDescription)
                .toArray(String[]::new);
        comboBox = new JComboBox<>(dataStrings);
        lastFilteredData = dataStrings;
        clearTextOnClick = true;
        isPopupClickable = true;
        this.defaultText = "    "+defaultText;
        comboBox.addPopupMenuListener(repaintListener);
        init();
    }

    private void init(){
        comboBox.setEditable(true);
        setText(defaultText);
        comboBox.setPreferredSize(size);
        comboBox.setMaximumRowCount(10);
        comboBox.setFont(textBoxFont);
        comboBox.setUI(new BasicComboBoxUI(){
            @Override
            protected JButton createArrowButton() {
                JButton but = new JButton();
                but.setBackground(Color.WHITE);
                but.setUI(new BasicButtonUI(){

                    @Override
                    public void paint(Graphics g, JComponent c) {

                        Graphics2D g2 = (Graphics2D) g;
                        g2.setColor(FavoriteColors.coolOrange);
                        g2.setStroke(new BasicStroke(2));
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        Shape triangle = new Polygon(
                                new int[]{(int) (2*c.getWidth()/6.0)-1, (int) (3*c.getWidth()/6.0), (int) (4*c.getWidth()/6.0)},
                                new int[]{c.getHeight()/3, (int) (2*c.getHeight()/3.0),c.getHeight()/3},3);
                        g2.fill(triangle);
                        super.paint(g2, c);
                    }

                    @Override
                    protected void paintButtonPressed(Graphics g, AbstractButton b) {

                        Graphics2D g2 = (Graphics2D) g;

                        g2.setColor(new Color(200,200,200, 128));
                        g2.fillRect(0,0,b.getWidth(),b.getHeight());

                        g2.setColor(FavoriteColors.coolOrange);
                        g2.setStroke(new BasicStroke(2));
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        Shape triangle = new Polygon(
                                new int[]{(int) (2*b.getWidth()/6.0)-1, (int) (3*b.getWidth()/6.0), (int) (4*b.getWidth()/6.0)},
                                new int[]{b.getHeight()/3, (int) (2*b.getHeight()/3.0),b.getHeight()/3},3);
                        g2.fill(triangle);
                        super.paintButtonPressed(g2, b);
                    }
                });
                but.setBorder(new Border() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke(2));
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(FavoriteColors.coolOrange);
                        g2.drawLine(0, (int) (height/6.0),0, (int) (5*height/6.0));
                    }

                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(0,0,0,0);
                    }

                    @Override
                    public boolean isBorderOpaque() {
                        return false;
                    }
                });
                but.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        but.setBackground(new Color(200,200,200, 64));
                        comboBox.revalidate();
                        comboBox.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        but.setBackground(Color.WHITE);
                        comboBox.revalidate();
                        comboBox.repaint();
                    }

                    @Override
                    public void mousePressed(MouseEvent e){
                        super.mousePressed(e);
                        if(clearTextOnClick){
                            setText("");
                            clearTextOnClick = false;
                        }
                        comboBox.revalidate();
                        comboBox.repaint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                        comboBox.revalidate();
                        comboBox.repaint();
                    }
                });

                return but;
            }

            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup pop = new BasicComboPopup(comboBox) {

                    @Override
                    protected JScrollPane createScroller() {
                        list.setFont(textBoxFont);
                        JScrollPane scroll = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                        scroll.setBackground(Color.WHITE);
                        scroll.getVerticalScrollBar().setUnitIncrement(30);
                        return scroll;
                    }

                    @SuppressWarnings({"rawtypes","unchecked"})
                    @Override
                    protected JList createList() {
                        JList list =  new JList(comboBox.getModel()) {
                            @Override
                            protected void processMouseEvent(MouseEvent e) {
                                // Check the flag to determine if mouse events should be processed
                                if (isPopupClickable) {
                                    super.processMouseEvent(e);
                                }
                            }

                            @Override
                            protected void processMouseMotionEvent(MouseEvent e) {
                                // Check the flag to determine if mouse events should be processed
                                if (isPopupClickable) {
                                    super.processMouseMotionEvent(e);
                                }
                            }

                        };

                        return list;
                    }
                };

                pop.setBorder(BorderFactory.createLineBorder(new Color(200,200,200),1));

                return pop;
            }

        });
        comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
                    e.consume(); // Consume the arrow key event
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                filterResults();
            }
        });
        comboBox.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(comboBox.getEditor().getItem().toString().equals("")){
                    setText(defaultText);
                    clearTextOnClick = true;
                }
            }
            public void focusGained(FocusEvent e) {
                if(clearTextOnClick){
                    setText("");
                }
            }
        });
        comboBox.addPopupMenuListener(new PopupMenuListener(){

            /**
             * This method is called before the popup menu becomes visible
             *
             * @param e a {@code PopupMenuEvent} containing the source of the event
             */
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                filterResults();
            }
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        comboBox.getEditor().getEditorComponent().addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if(clearTextOnClick){
                    clearTextOnClick = false;
                    setText("");
                }

                comboBox.showPopup();
            }
        });
        comboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

                setBackground(Color.WHITE);
                if(isSelected){
                    renderer.setBackground(new Color(200,200,200));
                }

                return renderer;
            }
        });
        comboBox.setBorder(new TextFieldBorder());
    }

    private void setText(String newText) {
        comboBox.getEditor().setItem(newText);
        text = newText;
        if(newText.equals("")) {
            comboBox.setSelectedItem(null);
        } else {
            comboBox.setSelectedItem(newText);
        }
    }
    private void filterResults() {
        String newText = comboBox.getEditor().getItem().toString().trim();

        if(newText.equals(text)){
            return;
        }
        String[] filteredData = data.stream()
                .filter(searchable -> searchable.isMatch(newText))
                .map(Searchable::getShortDescription)
                .toArray(String[]::new);

        if(lastFilteredData != null && lastFilteredData.length == filteredData.length){
            boolean isSame = true;
            for(int i = 0; i < filteredData.length; i++){
                if(!filteredData[i].equals(lastFilteredData[i])){
                    isSame = false;
                    break;
                }
            }
            if(isSame){
                return;
            }
        }

        if(filteredData.length == 0){
            if(comboBox.getModel().getElementAt(0).equals("No results found")){
                return;
            }
            filteredData = new String[]{"No results found"};
            isPopupClickable = false;
        } else {
            isPopupClickable = true;
        }

        comboBox.setModel(new DefaultComboBoxModel<>(filteredData));
        comboBox.setSelectedItem(newText);
        text = newText;
        lastFilteredData = filteredData;
        comboBox.showPopup();
    }

    @Override
    public Component getComponent() {
        return comboBox;
    }

    @Override
    public void componentResized(Dimension newSize) {
        // do nothing
    }

    public static void main(String[] args){


        //generate some data

        Searchable s1 = new Searchable() {
            final String description = "ItEm NuMbEr 1, beri cool. Case-insensitive Searchable example";
            @Override
            public boolean isMatch(String query) {
                return description.toLowerCase().contains(query.toLowerCase());
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }

        };

        Searchable s2 = new Searchable() {
            final String description = "Item number 2, muy suave. case-sensitive Searchable example";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }
        };

        Searchable s3 = new Searchable() {
            final String description = "Item number 3, un poco loco";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }
        };

        Searchable s4 = new Searchable() {
            final String description = "Item number 4, el gato es en la mesa";
            @Override
            public boolean isMatch(String query) {
                return description.contains(query);
            }

            @Override
            public String getShortDescription() {
                return description;
            }

            @Override
            public String getLongDescription() {
                return null;
            }
        };

        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        TransportBasePanel cp = new TransportBasePanel(){};
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cp.componentResized(frame.getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                cp.componentResized(frame.getSize());
            }
        });

        cp.getContentPanel().add(new SearchBox(List.of(s1, s2, s3, s4), "Enter site name or address", new Dimension(400, 30),cp).getComponent());
        JButton test = new JButton("test");
        cp.getContentPanel().add(test);
        frame.add(cp.getComponent());
        frame.pack();
        frame.setVisible(true);
        frame.requestFocus();
    }
}


