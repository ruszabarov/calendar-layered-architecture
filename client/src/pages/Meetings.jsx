import React, {useEffect, useState} from "react";
import axios from "axios";
import {API_URL} from "./Home";

const Meetings = () => {
    const [meetings, setMeetings] = useState([]);

    useEffect(() => {
        const fetchAttachments = async () => {
            try {
                // Replace 'your-api-endpoint' with the actual endpoint you want to call
                const response = await axios.get(`${API_URL}/meetings`);
                setMeetings(response.data);
            } catch (error) {
                console.error('Error fetching data: ', error);
            }
        };

        fetchAttachments();
    }, []);

    return (
        <div>
            {meetings.map((meeting, index) => (
                <div style={{marginBottom: '20px'}}>
                    <span>
                        <strong>UUID:</strong> {meeting.id} | <strong>Title:</strong> {meeting.title} | <strong>Date & Time:</strong> {meeting.dateTime} | <strong>Location:</strong> {meeting.location} | <strong>Details:</strong> {meeting.details}
                    </span>
                    <br></br>
                    <span style={{display: 'block'}}>
                        Calendars:
                    </span>
                    {meeting.calendars.map((calendar, index) => (
                        <span style={{display: 'block', marginLeft: '20px'}}>
                            <strong>UUID:</strong> {calendar.id} |
                            <strong> Title:</strong> {calendar.title} |
                            <strong> Details:</strong> {calendar.details} |
                        </span>
                    ))}
                    {meeting.participants.size > 0 &&
                        <span style={{display: 'block'}}>
                            Participants:
                        </span>
                    }

                    {meeting.participants.map((participant, index) => (
                        <span style={{display: 'block', marginLeft: '20px'}}>
                            <strong>UUID:</strong> {participant.id} |
                            <strong> Name:</strong> {participant.name} |
                            <strong> Email:</strong> {participant.email} |
                        </span>
                    ))}
                    {meeting.attachments.size > 0 &&
                        <span style={{display: 'block'}}>
                            Attachments:
                        </span>
                    }

                    {meeting.attachments.map((attachment, index) => (
                        <span style={{display: 'block', marginLeft: '20px'}}>
                            <strong>UUID:</strong> {attachment.id} |
                            <strong> URL:</strong> {attachment.url} |
                        </span>
                    ))}
                </div>
            ))}
        </div>
    )
}

export default Meetings;