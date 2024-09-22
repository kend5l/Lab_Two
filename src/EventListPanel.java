import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EventListPanel extends JPanel {
    private ArrayList<Event> events; // List of events
    private JPanel controlPanel;  // Panel for controls
    private JPanel displayPanel;  // Panel to display EventPanels
    private JComboBox<String> sortDropDown; // Dropdown to sort events
    private JComboBox<String> filterDropDown; // Dropdown to filter events by type
    private JCheckBox filterDisplay;  // Checkbox to filter completed events
    private JButton addEventButton;  // Button to add a new event

    // Constructor
    public EventListPanel() {
        events = new ArrayList<>();
        setLayout(new BorderLayout());  // Main layout manager

        // Create control panel and add it to the top
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // Sort dropdown options
        String[] sortOptions = {"Sort by Name", "Sort by Date", "Sort by Name (Reverse)", "Sort by Date (Reverse)"};
        sortDropDown = new JComboBox<>(sortOptions);
        sortDropDown.addActionListener(e -> sortEvents((String) sortDropDown.getSelectedItem())); // Use lambda for sorting
        controlPanel.add(sortDropDown);

        // Filter by event type dropdown
        String[] filterOptions = {"All Events", "Deadlines", "Meetings"};
        filterDropDown = new JComboBox<>(filterOptions);
        filterDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterEventsByType((String) filterDropDown.getSelectedItem());
            }
        });
        controlPanel.add(new JLabel("Filter by Type:")); // Label for clarity
        controlPanel.add(filterDropDown);

        // Filter checkbox for completed events
        filterDisplay = new JCheckBox("Show Completed Events");
        filterDisplay.setSelected(true);  // Show completed events by default
        filterDisplay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterEvents();
            }
        });
        controlPanel.add(filterDisplay);

        // Add event button
        addEventButton = new JButton("Add Event");
        addEventButton.setFont(new Font("Tahoma", Font.PLAIN, 12));

        // Open modal to add events
        addEventButton.addActionListener(e -> new AddEventModal(this));

        // Add button to controlPanel
        controlPanel.add(addEventButton);

        add(controlPanel, BorderLayout.NORTH);  // Add control panel at the top

        // Create display panel and add it to the center
        displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));  // Vertical list of events
        JScrollPane scrollPane = new JScrollPane(displayPanel);  // Add scrollable panel
        add(scrollPane, BorderLayout.CENTER);  // Add scrollable display panel in the center
    }

    // Method to add an Event to the list and update the display
    public void addEvent(Event event) {
        events.add(event);  // Add event to the list
        refreshDisplay();  // Update the display
    }

    // Method to refresh the display panel
    private void refreshDisplay() {
        displayPanel.removeAll();  // Clear the display panel
        for (Event event : events) {
            EventPanel eventPanel = new EventPanel(event);
            displayPanel.add(eventPanel);  // Add each event panel to the display
        }
        displayPanel.revalidate();  // Refresh the layout
        displayPanel.repaint();  // Redraw the display
    }

    // Method to sort events based on selected sorting option
    private void sortEvents(String sortOption) {
        switch (sortOption) {
            case "Sort by Name":
                Collections.sort(events, Comparator.comparing(Event::getName));
                break;
            case "Sort by Date":
                Collections.sort(events, Comparator.comparing(Event::getDateTime));
                break;
            case "Sort by Name (Reverse)":
                Collections.sort(events, Comparator.comparing(Event::getName).reversed());
                break;
            case "Sort by Date (Reverse)":
                Collections.sort(events, Comparator.comparing(Event::getDateTime).reversed());
                break;
        }
        refreshDisplay();  // Update the display after sorting
    }

    // Method to filter events based on the checkbox
    private void filterEvents() {
        displayPanel.removeAll();  // Clear the display panel
        for (Event event : events) {
            if (filterDisplay.isSelected() || !(event instanceof Completable) || !((Completable) event).isComplete()) {
                EventPanel eventPanel = new EventPanel(event);
                displayPanel.add(eventPanel);  // Add the panel if it matches the filter criteria
            }
        }
        displayPanel.revalidate();  // Refresh the layout
        displayPanel.repaint();  // Redraw the display
    }

    // Method to filter events by type (Deadlines or Meetings)
    private void filterEventsByType(String filterOption) {
        displayPanel.removeAll();  // Clear the display panel

        for (Event event : events) {
            if (filterOption.equals("All Events")) {
                displayEvent(event);  // Show all events
            } else if (filterOption.equals("Deadlines") && event instanceof Deadline) {
                displayEvent(event);  // Show only Deadline events
            } else if (filterOption.equals("Meetings") && event instanceof Meeting) {
                displayEvent(event);  // Show only Meeting events
            }
        }

        displayPanel.revalidate();  // Refresh the layout
        displayPanel.repaint();  // Redraw the display
    }

    // Helper method to display an event
    private void displayEvent(Event event) {
        EventPanel eventPanel = new EventPanel(event);
        displayPanel.add(eventPanel);
    }
}
