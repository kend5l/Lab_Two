import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddEventModal extends JDialog {
    private JTextField nameField;
    private JTextField dateTimeField;
    private JTextField endDateTimeField;  // Only for meetings
    private JTextField locationField;     // Only for meetings
    private JComboBox<String> eventTypeDropDown;
    private JButton submitButton;

    private EventListPanel eventListPanel;  // Reference to the EventListPanel to add events

    // Constructor to initialize the dialog
    public AddEventModal(EventListPanel eventListPanel) {
        this.eventListPanel = eventListPanel;
        setTitle("Add New Event");
        setSize(400, 300);
        setLayout(new GridLayout(7, 2));

        // Input fields
        nameField = new JTextField();
        dateTimeField = new JTextField("yyyy-MM-dd HH:mm");
        endDateTimeField = new JTextField("yyyy-MM-dd HH:mm");  // Only for Meeting
        locationField = new JTextField();  // Only for Meeting

        // Event type dropdown (Deadline or Meeting)
        eventTypeDropDown = new JComboBox<>(new String[]{"Deadline", "Meeting"});
        eventTypeDropDown.addActionListener(e -> toggleMeetingFields());

        // Submit button
        submitButton = new JButton("Add Event");
        submitButton.addActionListener(e -> addNewEvent());

        // Add components to dialog
        add(new JLabel("Event Name:"));
        add(nameField);
        add(new JLabel("Event Start Time:"));
        add(dateTimeField);
        add(new JLabel("Event End Time:"));
        add(endDateTimeField);
        add(new JLabel("Location:"));
        add(locationField);
        add(new JLabel("Event Type:"));
        add(eventTypeDropDown);
        add(submitButton);

        // Initial state: hide meeting-specific fields
        toggleMeetingFields();

        setVisible(true);
    }

    // Toggle visibility of Meeting-specific fields (endDateTime and location)
    private void toggleMeetingFields() {
        boolean isMeeting = eventTypeDropDown.getSelectedItem().equals("Meeting");
        endDateTimeField.setVisible(isMeeting);
        locationField.setVisible(isMeeting);
        revalidate();
        repaint();
    }

    // Method to add a new event based on the input fields
    private void addNewEvent() {
        String name = nameField.getText();
        LocalDateTime startDateTime = LocalDateTime.parse(dateTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // Check event type and add accordingly
        if (eventTypeDropDown.getSelectedItem().equals("Deadline")) {
            Deadline deadline = new Deadline(name, startDateTime);
            eventListPanel.addEvent(deadline);
        } else if (eventTypeDropDown.getSelectedItem().equals("Meeting")) {
            LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String location = locationField.getText();
            Meeting meeting = new Meeting(name, startDateTime, endDateTime, location);
            eventListPanel.addEvent(meeting);
        }

        dispose();  // Close the dialog
    }
}
