import React, { useEffect, useState } from "react";
import axios from "axios";
import { API_URL } from "./Home";


const Participants = () => {
    // useStates
    const [participants, setParticipants] = useState([]);
    const [isEditing, setIsEditing] = useState(false);
    const [editIndex, setEditIndex] = useState(null);


    const [editingElement, setEditingElement] = useState({
        id: '',
        participantName: '',
        participantEmail: '',
        meetingId: ''
    });


    // Fetch participants data
    const fetchParticipants = async () => {
        try {
            const response = await axios.get(`${API_URL}/participants`);
            setParticipants(response.data);
        } catch (error) {
            console.error('Error fetching data: ', error);
        }
    };


    useEffect(() => {
        fetchParticipants();
    }, []);


    // Validation for input
    const validateInputs = () => {
        // Participant name cannot be more than 600 char
        if (editingElement.participantName.length > 600) {
            alert('Name should not exceed 600 characters.');
            return false;
        }
        // Not sure how it works but ensures that there is a valid email
        if (!/\S+@\S+\.\S+/.test(editingElement.participantEmail)) {
            alert('Please enter a valid email address.');
            return false;
        }
        return true;
    };


    // Handle create or update participant
    const handleCreateOrUpdate = async () => {
        if (!validateInputs()) return;


        if (isEditing) {
            await axios.put(`${API_URL}/participants/${editingElement.id}`, editingElement);
        } else {
            await axios.post(`${API_URL}/participants`, editingElement);
        }
        fetchParticipants();
        resetForm();
    };


    // Edit mode
    const handleEdit = (index, item) => {
        setIsEditing(true);
        setEditIndex(index);
        setEditingElement(item);
    };


    // Deletes id
    const handleDelete = async (id) => {
        await axios.delete(`${API_URL}/participants/${id}`);
        fetchParticipants();
    };


    // Handles the change
    const handleInputChange = (e) => {
        setEditingElement({ ...editingElement, [e.target.name]: e.target.value });
    };


    // Resets the form
    const resetForm = () => {
        setEditingElement({
            id: '',
            participantName: '',
            participantEmail: '',
            meetingId: ''
        });
        setIsEditing(false);
        setEditIndex(null);
    };


    return (
        <div>
            <div className="form">
                <input name="uuid" value={editingElement.id} onChange={handleInputChange} placeholder="Participant UUID" />
                <input name="participantName" value={editingElement.participantName} onChange={handleInputChange} placeholder="Name (max 600 characters)" />
                <input name="participantEmail" value={editingElement.participantEmail} onChange={handleInputChange} placeholder="Email" />
                <input name="meetingId" value={editingElement.meetingId} onChange={handleInputChange} placeholder="Associated Meeting ID" />
            </div>


            <button onClick={handleCreateOrUpdate}>
                {isEditing ? 'Save Changes' : 'Create'}
            </button>


            {participants.map((participant, index) => (
                <div key={index} style={{ marginBottom: '20px' }}>
                    <span>
                        <strong>UUID:</strong> {participant.id} |
                        <strong> Name:</strong> {participant.participantName} |
                        <strong> Email:</strong> {participant.participantEmail} |
                        <strong> Meeting ID:</strong> {participant.meetingId}
                    </span>
                    <button onClick={() => handleEdit(index, participant)}>Edit</button>
                    <button onClick={() => handleDelete(participant.id)}>Delete</button>
                </div>
            ))}
        </div>
    );
};


export default Participants;