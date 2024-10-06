import React, {useEffect, useState} from "react";
import axios from "axios";
import {API_URL} from "./Home";

const Attachments = () => {
    const [attachments, setAttachments] = useState([]);

    useEffect(() => {
        const fetchAttachments = async () => {
            try {
                // Replace 'your-api-endpoint' with the actual endpoint you want to call
                const response = await axios.get(`${API_URL}/attachments`);
                setAttachments(response.data);
            } catch (error) {
                console.error('Error fetching data: ', error);
            }
        };

        fetchAttachments();
    }, []);

    return (
        <div>
            {attachments.map((attachment, index) => (
                <div>
                    <span>
                        <strong>UUID:</strong> {attachment.id} | <strong>URL:</strong> {attachment.url}
                    </span>
                    <br></br>
                    <span>
                        Meetings:
                    </span>
                    {attachment.meetings.map((meeting, index) => (
                        <span>
                            <strong>UUID:</strong> {meeting.id} |
                            <strong>Title:</strong> {meeting.title} |
                            <strong>Details:</strong> {meeting.details} |
                            <strong>Location:</strong> {meeting.location}
                        </span>
                    ))}

                </div>
            ))}
        </div>
    )
}

export default Attachments;