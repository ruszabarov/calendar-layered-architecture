import React, {useState, useEffect} from 'react';
import axios from 'axios';
import {v4 as uuidv4} from 'uuid';
import './Home.css';
import Attachments from "./Attachments";
import Meetings from "./Meetings";

export const API_URL = 'http://localhost:3000'; // Replace with the correct backend URL

const Home = () => {
    const [currentTab, setCurrentTab] = useState('Meetings');
    const [meetings, setMeetings] = useState([]);
    const [calendars, setCalendars] = useState([]);
    const [participants, setParticipants] = useState([]);
    const [inputData, setInputData] = useState({
        uuid: '',
        title: '',
        details: '',
        dateTime: '',
        location: '',
        participantName: '',
        participantEmail: '',
        url: '',
        meetingId: '',
        calendarIds: '',
        participantIds: '',
        attachmentIds: '',
        meetingIds: '',
    });
    const [isEditing, setIsEditing] = useState(false);
    const [editIndex, setEditIndex] = useState(null);

    // Fetch data on initial load
    useEffect(() => {
        fetchData();
    }, [currentTab]);

    // Fetches data to send data
    const fetchData = async () => {
        // Meetings
        if (currentTab === 'Meetings') {
            const response = await axios.get(`${API_URL}/meetings`);
            setMeetings(response.data);
        }

        // Calendars
        if (currentTab === 'Calendars') {
            const response = await axios.get(`${API_URL}/calendars`);
            setCalendars(response.data);
        }

        // Participants
        if (currentTab === 'Participants') {
            const response = await axios.get(`${API_URL}/participants`);
            setParticipants(response.data);
        }
    };

    const handleInputChange = (e) => {
        setInputData({...inputData, [e.target.name]: e.target.value});
    };

    // Validations
    const validateInputs = () => {
        // Ensures Meeting information is in correct format
        if (currentTab === 'Meetings') {
            return inputData.title.length <= 2000 &&
                /^\d{4}-\d{2}-\d{2} \d{2}:\d{2} (AM|PM)$/.test(inputData.dateTime) &&
                inputData.location.length <= 2000 &&
                inputData.details.length <= 10000;
        }
        // Ensures Calendars information is in correct format
        if (currentTab === 'Calendars') {
            return inputData.title.length <= 2000 && inputData.details.length <= 10000;
        }
        // Ensures Participant information is in correct format
        if (currentTab === 'Participants') {
            return inputData.participantName.length <= 600 && /\S+@\S+\.\S+/.test(inputData.participantEmail);
        }
        // Ensures Attachment information is in correct format
        if (currentTab === 'Attachments') {
            return inputData.url.startsWith('http');
        }
        return true;
    };

    // Creates only if inputs are valid
    const handleCreate = async () => {
        if (!validateInputs()) {
            alert('Invalid input values. Please correct the errors.');
            return;
        }

        // Generates uuid if blank
        const newItem = {...inputData, uuid: inputData.uuid || uuidv4()};

        // Post requests
        // Meetings
        if (currentTab === 'Meetings') {

        }
        // Calendars
        if (currentTab === 'Calendars') {
            await axios.post(`${API_URL}/calendars`, newItem);
        }
        // Participants
        if (currentTab === 'Participants') {
            await axios.post(`${API_URL}/participants`, newItem);
        }
        // Attachments
        if (currentTab === 'Attachments') {
            await axios.post(`${API_URL}/attachments`, newItem);
        }
        // Fetches / reloads data
        fetchData();
        // Resets form
        resetForm();
    };

    // Handles delete
    const handleDelete = async (index, id) => {
        if (currentTab === 'Meetings') await axios.delete(`${API_URL}/meetings/${id}`);
        if (currentTab === 'Calendars') await axios.delete(`${API_URL}/calendars/${id}`);
        if (currentTab === 'Participants') await axios.delete(`${API_URL}/participants/${id}`);
        if (currentTab === 'Attachments') await axios.delete(`${API_URL}/attachments/${id}`);
        fetchData();
    };

    // Handles edit
    const handleEdit = (index, item) => {
        setIsEditing(true);
        setEditIndex(index);
        setInputData(item);
    };

    // Handles update
    const handleUpdate = async () => {
        if (!validateInputs()) {
            alert('Invalid input values. Please correct the errors.');
            return;
        }
        const updatedItem = {...inputData};
        if (currentTab === 'Meetings') await axios.put(`${API_URL}/meetings/${updatedItem.uuid}`, updatedItem);
        if (currentTab === 'Calendars') await axios.put(`${API_URL}/calendars/${updatedItem.uuid}`, updatedItem);
        if (currentTab === 'Participants') await axios.put(`${API_URL}/participants/${updatedItem.uuid}`, updatedItem);
        if (currentTab === 'Attachments') await axios.put(`${API_URL}/attachments/${updatedItem.uuid}`, updatedItem);
        fetchData();
        resetForm();
    };

    // Reset form function
    const resetForm = () => {
        setInputData({
            uuid: '',
            title: '',
            details: '',
            dateTime: '',
            location: '',
            participantName: '',
            participantEmail: '',
            url: '',
            meetingId: '',
            calendarIds: '',
            participantIds: '',
            attachmentIds: '',
            meetingIds: '',
        });
        setIsEditing(false);
        setEditIndex(null);
    };

    return (
        <div className="container">

            {/* TABS */}
            <div className="tabs">
                <button onClick={() => setCurrentTab('Meetings')}>Meetings</button>
                <button onClick={() => setCurrentTab('Calendars')}>Calendars</button>
                <button onClick={() => setCurrentTab('Participants')}>Participants</button>
                <button onClick={() => setCurrentTab('Attachments')}>Attachments</button>
            </div>

            <div className="content">
                <h2>{currentTab}</h2>
                <div className="form">

                    {/* Meeting variables --> have to use text area because of glitch for now */}
                    {currentTab === 'Meetings' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange}
                                   placeholder="Meeting UUID"/>
                            <input name="title" value={inputData.title} onChange={handleInputChange}
                                   placeholder="Title"/>
                            <input name="dateTime" value={inputData.dateTime} onChange={handleInputChange}
                                   placeholder="Date and Time (YYYY-MM-DD HH:MM AM/PM)"/>
                            <input name="location" value={inputData.location} onChange={handleInputChange}
                                   placeholder="Location"/>
                            <textarea name="details" value={inputData.details} onChange={handleInputChange}
                                      placeholder="Details"/>
                            <input name="calendarIds" value={inputData.calendarIds} onChange={handleInputChange}
                                   placeholder="Calendar IDs (comma-separated)"/>
                            <input name="participantIds" value={inputData.participantIds} onChange={handleInputChange}
                                   placeholder="Participant IDs (comma-separated)"/>
                            <input name="attachmentIds" value={inputData.attachmentIds} onChange={handleInputChange}
                                   placeholder="Attachment IDs (comma-separated)"/>
                        </>
                    )}

                    {/* Calendar variables --> have to use textarea because of glitch for now */}
                    {currentTab === 'Calendars' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange}
                                   placeholder="Calendar UUID"/>
                            <input name="title" value={inputData.title} onChange={handleInputChange}
                                   placeholder="Calendar Title"/>
                            <textarea name="details" value={inputData.details} onChange={handleInputChange}
                                      placeholder="Calendar Details"/>
                            <input name="meetingIds" value={inputData.meetingIds} onChange={handleInputChange}
                                   placeholder="Meeting IDs (comma-separated)"/>
                        </>
                    )}

                    {/* Participant variables */}
                    {currentTab === 'Participants' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange}
                                   placeholder="Participant UUID"/>
                            <input name="meetingId" value={inputData.meetingId} onChange={handleInputChange}
                                   placeholder="Meeting ID"/>
                            <input name="participantName" value={inputData.participantName} onChange={handleInputChange}
                                   placeholder="Participant Name"/>
                            <input name="participantEmail" value={inputData.participantEmail}
                                   onChange={handleInputChange} placeholder="Participant Email"/>
                        </>
                    )}

                    {/* Attachment variables */}
                    {currentTab === 'Attachments' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange}
                                   placeholder="Attachment UUID"/>
                            <input name="meetingId" value={inputData.meetings} onChange={handleInputChange}
                                   placeholder="Meeting ID"/>
                            <input name="url" value={inputData.url} onChange={handleInputChange}
                                   placeholder="Attachment URL"/>
                        </>
                    )}

                    {/* Switches between update mode and create mode on selected tab */}
                    <button
                        onClick={isEditing ? handleUpdate : handleCreate}>{isEditing ? 'Save Changes' : 'Create'}</button>
                </div>

                <div className="list">

                    {/* Meetings */}
                    {currentTab === 'Meetings' && <Meetings/>}

                    {/* Calendars */}
                    {currentTab === 'Calendars' && calendars.map((calendar, index) => (
                        <div key={index} className="item">
                            <span>
                                <strong>UUID:</strong> {calendar.uuid} | <strong>Title:</strong> {calendar.title} | <strong>Details:</strong> {calendar.details} | <strong>Meeting IDs:</strong> {calendar.meetingIds}
                            </span>
                            <button onClick={() => handleEdit(index)}>Edit</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}

                    {/* Participants */}
                    {currentTab === 'Participants' && participants.map((participant, index) => (
                        <div key={index} className="item">
                            <span>
                                <strong>UUID:</strong> {participant.uuid} | <strong>Meeting ID:</strong> {participant.meetingId} | <strong>Name:</strong> {participant.participantName} | <strong>Email:</strong> {participant.participantEmail}
                            </span>
                            <button onClick={() => handleEdit(index)}>Edit</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}

                    {/* Attachments */}
                    {currentTab === 'Attachments' && <Attachments/>}
                </div>
            </div>
        </div>
    );
};

export default Home;
