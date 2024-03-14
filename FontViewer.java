//
// Name: Rodriguez, Jonathan
// Project: #3
// Due: 03/15/24
// Course: cs-2450-01-sp24
//
// Description: A program that can be used to explore various font styles,
// sizes, and effects. Users can interactively select fonts, adjust text size,
// choose between regular, italic, and bold styles, and apply effects such as
// all caps, with real-time previews of the changes.
//

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class FontViewer implements ItemListener {
    private final String DEFAULT_SAMPLE = "a quick brown fox jumps over the lazy dog 0123456789";
    private JLabel fontDisplay;
    private String originalText;
    private JList<String> fontList;
    public FontViewer(String initString) {

        // Set the originalText to the initString if it's not null or empty, otherwise use the default sample
        originalText = (initString != null && !initString.isEmpty()) ? initString : DEFAULT_SAMPLE;


        // Creating the frame for the font viewer
        JFrame frame = new JFrame("Font Viewer");
        frame.setIconImage(new ImageIcon("fontviewer.png").getImage());
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create padding border
        Border paddingBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

        // Creating a panel for size controls
        JPanel sizePanel = new JPanel(new BorderLayout());
        sizePanel.setBorder(paddingBorder);

        // Creating a label for the slider
        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setDisplayedMnemonic('s');


        // Creating a slider with values from 8 to 20
        JSlider sizeSlider = new JSlider(JSlider.HORIZONTAL, 8, 20, 12);
        sizeSlider.setMajorTickSpacing(2);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setSnapToTicks(true);
        sizeLabel.setLabelFor(sizeSlider); //Associate label with slider

        //Adding components to size panel
        sizePanel.add(sizeLabel, BorderLayout.NORTH);
        sizePanel.add(sizeSlider, BorderLayout.CENTER);
        frame.add(sizePanel, BorderLayout.NORTH); //Add to frame

        //Creating a panel for font list
        JPanel fontPanel = new JPanel(new BorderLayout());
        fontPanel.setBorder(paddingBorder);

        // Creating a label for the list
        JLabel fontLabel = new JLabel("Fonts:");
        fontLabel.setDisplayedMnemonic('f');

        // Creating a scrollable list of fonts
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontList = new JList<>(fontNames);

        //Only one font can be selected
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Make the list scrollable
        JScrollPane scrollPane = new JScrollPane(fontList);
        fontLabel.setLabelFor(scrollPane); //Associate label with scroll pane

        //Adding components to font panel
        fontPanel.add(fontLabel, BorderLayout.NORTH);
        fontPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(fontPanel, BorderLayout.WEST);

        //Creating a panel for style section
        JPanel stylePanel = new JPanel(new BorderLayout());
        stylePanel.setBorder(paddingBorder);

        //Creating a label for the style section
        JLabel styleLabel = new JLabel("Style:");

        //Creating style options panel
        JPanel styleOptionsPanel = new JPanel(new GridLayout(3, 1));

        //Regular text option
        JRadioButton regular = new JRadioButton("Regular");
        regular.setSelected(true); //Enabled by default
        regular.addItemListener(this);
        regular.setMnemonic('r');
        //Italic text option
        JRadioButton italic = new JRadioButton("Italic");
        italic.addItemListener(this);
        italic.setMnemonic('i');
        //Bold text option
        JRadioButton bold = new JRadioButton("Bold");
        bold.addItemListener(this);
        bold.setMnemonic('b');

        //Adding the buttons to a group
        ButtonGroup styleGroup = new ButtonGroup();
        styleGroup.add(regular);
        styleGroup.add(italic);
        styleGroup.add(bold);


        //Adding everything to the style optional panel
        styleOptionsPanel.add(regular);
        styleOptionsPanel.add(italic);
        styleOptionsPanel.add(bold);

        //Putting everything into the style section panel
        stylePanel.add(styleLabel, BorderLayout.NORTH);
        stylePanel.add(styleOptionsPanel, BorderLayout.CENTER);
        frame.add(stylePanel, BorderLayout.CENTER); //Add to frame

        //Creating a panel for effects
        JPanel effectsPanel = new JPanel(new BorderLayout());
        effectsPanel.setBorder(paddingBorder);

        //Creating a label for effects
        JLabel effectsLabel = new JLabel("Effects:");
        effectsPanel.add(effectsLabel, BorderLayout.NORTH);

        //Creating effects options
        JPanel effectsOptionsPanel = new JPanel(new GridLayout(1, 1));
        JCheckBox caps = new JCheckBox("All caps");
        caps.addItemListener(this);
        caps.setMnemonic('c');
        effectsOptionsPanel.add(caps);
        effectsPanel.add(effectsOptionsPanel, BorderLayout.CENTER);
        frame.add(effectsPanel, BorderLayout.EAST);


        //Label to show font
        fontDisplay = new JLabel(originalText);
        fontDisplay.setBorder(paddingBorder);
        //Set to regular style by default
        fontDisplay.setFont(fontDisplay.getFont().deriveFont(Font.PLAIN));
        fontDisplay.setHorizontalAlignment(JLabel.CENTER);
        frame.add(fontDisplay, BorderLayout.SOUTH);

        // Add a ListSelectionListener to the fontList
        fontList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected font name
                    String selectedFontName = fontList.getSelectedValue();
                    Font currentFont = fontDisplay.getFont();
                    Font newFont = new Font(selectedFontName, currentFont.getStyle(), currentFont.getSize());
                    fontDisplay.setFont(newFont);
                }
            }
        });

        // Add a ChangeListener to the sizeSlider
        sizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    int size = source.getValue();
                    //Update fontDisplay size
                    Font currentFont = fontDisplay.getFont();
                    Font newFont = currentFont.deriveFont((float) size);
                    fontDisplay.setFont(newFont);
                }
            }
        });


        // Letting pack handle the sizing
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Set default selection to "Dialog" font
        fontList.setSelectedValue("Dialog", true);
        fontList.ensureIndexIsVisible(fontList.getSelectedIndex());

    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (source instanceof JCheckBox) {
            //Check if caps is enabled
            if (e.getStateChange() == ItemEvent.SELECTED) {
                //Put caps on the text
                fontDisplay.setText(originalText.toUpperCase());
            } else {
                fontDisplay.setText(originalText);
            }
        } else if (source instanceof JRadioButton chosenOption) {
            if (chosenOption.getText().equals("Regular")) {
                fontDisplay.setFont(fontDisplay.getFont().deriveFont(Font.PLAIN));
            } else if (chosenOption.getText().equals("Italic")) {
                fontDisplay.setFont(fontDisplay.getFont().deriveFont(Font.ITALIC));
            } else if (chosenOption.getText().equals("Bold")) {
                fontDisplay.setFont(fontDisplay.getFont().deriveFont(Font.BOLD));
            }
        } else if (source instanceof JList) {
            // If the event source is the fontList
            String selectedFontName = fontList.getSelectedValue();
            Font currentFont = fontDisplay.getFont();
            Font newFont = new Font(selectedFontName, currentFont.getStyle(), currentFont.getSize());
            fontDisplay.setFont(newFont);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FontViewer(args.length != 0 ? args[0] : null));
    }



}
