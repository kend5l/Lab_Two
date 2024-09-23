import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventPanel extends JPanel {

    private Event event;  // The event to display
    private JButton completeButton;  // Button to mark event as complete (if Completable)

    // Constructor to initialize EventPanel with an Event object
    public EventPanel(Event event) {
        this.event = event;
        setLayout(new GridLayout(0, 1));  // Vertical layout

        // Add labels and complete button (if applicable)
        addEventDetails();
        updateUrgency();
    }

    // Method to add event details to the panel
    private void addEventDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Add event name
        JLabel nameLabel = new JLabel("Event Name: " + event.getName());
        add(nameLabel);

        // Add event time
        JLabel timeLabel = new JLabel("Event Time: " + event.getDateTime().format(formatter));
        add(timeLabel);


        // If the event is a Meeting, add end time, location, and duration
        if (event instanceof Meeting) {
            Meeting meeting = (Meeting) event;

            JLabel endTimeLabel = new JLabel("End Time: " + meeting.getEndDateTime().format(formatter));
            add(endTimeLabel);

            JLabel locationLabel = new JLabel("Location: " + meeting.getLocation());
            add(locationLabel);

            JLabel durationLabel = new JLabel("Duration: " + meeting.getDuration() + " minutes");
            add(durationLabel);
        }

        // If the event is Completable, add the completion status and a completion checkbox
        if (event instanceof Completable) {
            Completable completableEvent = (Completable) event;

            // Create a checkbox to represent the completion status
            JCheckBox completeCheckBox = new JCheckBox("Complete", completableEvent.isComplete());
            add(completeCheckBox);

            // Disable the checkbox if the event is already complete
            if (completableEvent.isComplete()) {
                completeCheckBox.setEnabled(false);
            }

            // Add a listener to the checkbox
            completeCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // When the checkbox is selected, mark the event as complete
                    if (completeCheckBox.isSelected()) {
                        completableEvent.complete();
                        completeCheckBox.setEnabled(false);  // Disable the checkbox after marking complete
                    }
                }
            });
        }


    }


    // Method to update the panel background color based on event urgency
    public void updateUrgency() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventTime = event.getDateTime();

        // Calculate the time difference between now and the event
        Duration duration = Duration.between(now, eventTime);

        // Set the background color based on the urgency of the event
        if (duration.isNegative()) {
            // Event is overdue
            setBackground(Color.RED);
        } else if (duration.toHours() <= 1) {
            // Event is imminent (less than or equal to 1 hour away)
            setBackground(Color.YELLOW);
        } else {
            // Event is distant
            setBackground(Color.GREEN);
        }
    }
}

