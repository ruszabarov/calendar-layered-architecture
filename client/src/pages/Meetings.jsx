import React, {useEffect, useState} from "react";
import axios from "axios";
import {API_URL} from "./Home";

const Meetings = () => {
    const [meetings, setMeetings] = useState([]);
    const [isEditing, setIsEditing] = useState(false);
    const [editIndex, setEditIndex] = useState(null);

    const [editingElement, setEditingElement] = useState({
        id: '',
        title: '',
        details: '',
        dateTime: '',
        location: '',
    });

    const fetchMeetings = async () => {
        try {
            // Replace 'your-api-endpoint' with the actual endpoint you want to call
            const response = await axios.get(`${API_URL}/meetings`);
            setMeetings(response.data);
        } catch (error) {
            console.error('Error fetching data: ', error);
        }
    };

    useEffect(() => {
        fetchMeetings();
    }, []);

    const handleCreate = async () => {
        await axios.post(`${API_URL}/attachments`, editingElement);
        fetchMeetings();
        resetForm();
    }

    const handleEdit = (index, item) => {
        setIsEditing(true);
        setEditIndex(index);
        setEditingElement(item);
    };

    const handleDelete = async (index, id) => {
        await axios.delete(`${API_URL}/meetings/${id}`);
        fetchMeetings();
    };

    const handleInputChange = (e) => {
        setEditingElement({...editingElement, [e.target.name]: e.target.value});
    };

    const handleUpdate = async () => {
        const updatedItem = {...editingElement};
        await axios.put(`${API_URL}/meetings/${updatedItem.uuid}`, updatedItem);
        fetchMeetings();
        resetForm();
    };

    const resetForm = () => {
        setEditingElement({
            id: '',
            title: '',
            details: '',
            dateTime: '',
            location: '',
        });
        setIsEditing(false);
        setEditIndex(null);
    };

    return (
        <div>
            <div className="form">
                <input name="uuid" value={editingElement.id} onChange={handleInputChange}
                       placeholder="Meeting UUID"/>
                <input name="title" value={editingElement.title} onChange={handleInputChange}
                       placeholder="Title"/>
                <input name="dateTime" value={editingElement.dateTime} onChange={handleInputChange}
                       placeholder="Date and Time (YYYY-MM-DD HH:MM AM/PM)"/>
                <input name="location" value={editingElement.location} onChange={handleInputChange}
                       placeholder="Location"/>
                <textarea name="details" value={editingElement.details} onChange={handleInputChange}
                          placeholder="Details"/>
            </div>

            <button onClick={isEditing ? handleUpdate : handleCreate}>
                {isEditing ? 'Save Changes' : 'Create'}
            </button>

            {meetings.map((meeting, index) => (
                <div style={{marginBottom: '20px'}}>
                    <span>
                        <strong>UUID:</strong> {meeting.id} | <strong>Title:</strong> {meeting.title} | <strong>Date & Time:</strong> {meeting.dateTime} | <strong>Location:</strong> {meeting.location} | <strong>Details:</strong> {meeting.details}
                    </span>
                    <br></br>
                    {meeting.calendars.length > 0 &&
                        <span style={{display: 'block'}}>
                        Calendars:
                        </span>
                    }
                    {meeting.calendars.map((calendar, index) => (
                        <span style={{display: 'block', marginLeft: '20px'}}>
                            <strong>UUID:</strong> {calendar.id} |
                            <strong> Title:</strong> {calendar.title} |
                            <strong> Details:</strong> {calendar.details} |
                        </span>
                    ))}
                    {meeting.participants.length > 0 &&
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
                    {meeting.attachments.length > 0 &&
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
                    <button onClick={() => handleEdit(index, meeting)}>Edit</button>
                    <button onClick={() => handleDelete(index)}>Delete</button>
                </div>
            ))
            }
        </div>
    )
}

export default Meetings;