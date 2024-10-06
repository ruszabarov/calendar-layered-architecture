import React, { useEffect, useState } from "react";
import axios from "axios";
import { API_URL } from "./Home";

const Calendars = () => {
    // useStates
    const [calendars, setCalendars] = useState([]);
    const [isEditing, setIsEditing] = useState(false);
    const [editIndex, setEditIndex] = useState(null);

    const [editingElement, setEditingElement] = useState({
        id: '',
        title: '',
        details: '',
        meetingIds: ''
    });

    // Fetch calendar data
    const fetchCalendars = async () => {
        try {
            const response = await axios.get(`${API_URL}/calendars`);
            setCalendars(response.data);
        } catch (error) {
            console.error("Error fetching calendars data: ", error);
        }
    };

    // Use effect fetches calendar data from the server
    useEffect(() => {
        fetchCalendars();
    }, []);

    // Validations
    const validateInputs = () => {
        // Title is not longer than 2000 chars
        if (editingElement.title.length > 2000) {
            alert("Title should not exceed 2000 characters.");
            return false;
        }
        // Details is not longer than 10000 chars
        if (editingElement.details.length > 10000) {
            alert("Details should not exceed 10000 characters.");
            return false;
        }
        return true;
    };

    // Handle creation or update of a calendar
    const handleCreateOrUpdate = async () => {
        if (!validateInputs()) return;

        if (isEditing) {
            await axios.put(`${API_URL}/calendars/${editingElement.id}`, editingElement);
        } else {
            await axios.post(`${API_URL}/calendars`, editingElement);
        }
        fetchCalendars();
        resetForm();
    };

    // Edit mode
    const handleEdit = (index, item) => {
        setIsEditing(true);
        setEditIndex(index);
        setEditingElement(item);
    };

    // Handles delete
    const handleDelete = async (id) => {
        await axios.delete(`${API_URL}/calendars/${id}`);
        fetchCalendars();
    };

    // Handles the change
    const handleInputChange = (e) => {
        setEditingElement({ ...editingElement, [e.target.name]: e.target.value });
    };

    // Resets form
    const resetForm = () => {
        setEditingElement({
            id: '',
            title: '',
            details: '',
            meetingIds: ''
        });
        setIsEditing(false);
        setEditIndex(null);
    };

    return (
        <div>
            <div className="form">
                <input name="uuid" value={editingElement.id} onChange={handleInputChange} placeholder="Calendar UUID" />
                <input name="title" value={editingElement.title} onChange={handleInputChange} placeholder="Title (max 2000 characters)" />
                <textarea name="details" value={editingElement.details} onChange={handleInputChange} placeholder="Details (max 10000 characters)" />
                <input name="meetingIds" value={editingElement.meetingIds} onChange={handleInputChange} placeholder="Meeting IDs (comma-separated)" />
            </div>

            <button onClick={handleCreateOrUpdate}>
                {isEditing ? 'Save Changes' : 'Create'}
            </button>

            {calendars.map((calendar, index) => (
                <div key={index} style={{ marginBottom: '20px' }}>
                    <span>
                        <strong>UUID:</strong> {calendar.id} | 
                        <strong> Title:</strong> {calendar.title} | 
                        <strong> Details:</strong> {calendar.details} | 
                        <strong> Meeting IDs:</strong> {calendar.meetingIds}
                    </span>
                    
                    {calendar.meetings && calendar.meetings.length > 0 && (
                        <span style={{ display: 'block', marginTop: '10px' }}>
                            <strong>Meetings:</strong>
                        </span>
                    )}
                    {calendar.meetings && calendar.meetings.map((meeting, index) => (
                        <span key={index} style={{ display: 'block', marginLeft: '20px' }}>
                            <strong>UUID:</strong> {meeting.id} | 
                            <strong> Title:</strong> {meeting.title} | 
                            <strong> Date & Time:</strong> {meeting.dateTime} | 
                            <strong> Location:</strong> {meeting.location} | 
                            <strong> Details:</strong> {meeting.details}
                        </span>
                    ))}
                    
                    <button onClick={() => handleEdit(index, calendar)}>Edit</button>
                    <button onClick={() => handleDelete(calendar.id)}>Delete</button>
                </div>
            ))}
        </div>
    );
};

export default Calendars;
