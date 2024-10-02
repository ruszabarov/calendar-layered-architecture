import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';
import './Home.css';

const Home = () => {
    // Starts the user off on Meetings but can be switched to the other tabs
    // ex. Calendars, Participants, Attachments
    const [currentTab, setCurrentTab] = useState('Meetings');

    // State for each entity
    const [meetings, setMeetings] = useState([]);
    const [calendars, setCalendars] = useState([]);
    const [participants, setParticipants] = useState([]);
    const [attachments, setAttachments] = useState([]);

    // Use States
    const [inputData, setInputData] = useState({
        uuid: '',
        title: '',
        details: '',
        dateTime: '',
        location: '',
        participantName: '',
        participantEmail: '',
        attachmentUrl: '',
        meetingId: ''
    });

    // New states to handle update mode and tracking which item is being edited
    const [isEditing, setIsEditing] = useState(false);
    const [editIndex, setEditIndex] = useState(null);

    // Handle input changes
    const handleInputChange = (e) => {
        setInputData({ ...inputData, [e.target.name]: e.target.value });
    };

    // Validation for each tab based on rules
    const validateInputs = () => {
        // Ensures that the title is not more than 2000 characters
        // Ensures that the Date and Time is in YYYY-MM-DD HH:MM AM/PM form (not fully sure how it works but it does)
        // Ensures that location is not more than 2000 characters
        // Ensures that details is not more than 10000 characters
        if (currentTab === 'Meetings') {
            return inputData.title.length <= 2000 &&
                inputData.dateTime.match(/\d{4}-\d{2}-\d{2} \d{2}:\d{2} (AM|PM)/) &&
                inputData.location.length <= 2000 &&
                inputData.details.length <= 10000;
        }
        // Ensures that the title is not more than 2000 characters
        // Ensures that the details is not more than 10000 characters
        if (currentTab === 'Calendars') {
            return inputData.title.length <= 2000 && inputData.details.length <= 10000;
        }
        // Ensures that the name of the participant is not more than 600 characters
        if (currentTab === 'Participants') {
            return inputData.participantName.length <= 600 &&
                /\S+@\S+\.\S+/.test(inputData.participantEmail);
        }
        // Ensures that it is a URL
        if (currentTab === 'Attachments') {
            return inputData.attachmentUrl.startsWith('http');
        }
        return true;
    };

    // CRUD functions
    // Gives an alert if there is an error
    const handleCreate = () => {
        if (!validateInputs()) {
            alert('Invalid input values. Please correct the errors.');
            return;
        }

        const newItem = { ...inputData, uuid: inputData.uuid || uuidv4() };
        if (currentTab === 'Meetings') setMeetings([...meetings, newItem]);
        if (currentTab === 'Calendars') setCalendars([...calendars, newItem]);
        if (currentTab === 'Participants') setParticipants([...participants, newItem]);
        if (currentTab === 'Attachments') setAttachments([...attachments, newItem]);
        resetForm();  // Resets the form
    };

    // Deletes the current 'record'
    const handleDelete = (index) => {
        if (currentTab === 'Meetings') setMeetings(meetings.filter((_, i) => i !== index));
        if (currentTab === 'Calendars') setCalendars(calendars.filter((_, i) => i !== index));
        if (currentTab === 'Participants') setParticipants(participants.filter((_, i) => i !== index));
        if (currentTab === 'Attachments') setAttachments(attachments.filter((_, i) => i !== index));
    };

    // Handles the update
    const handleEdit = (index) => {
        setIsEditing(true);
        setEditIndex(index);
        if (currentTab === 'Meetings') setInputData(meetings[index]);
        if (currentTab === 'Calendars') setInputData(calendars[index]);
        if (currentTab === 'Participants') setInputData(participants[index]);
        if (currentTab === 'Attachments') setInputData(attachments[index]);
    };

    // Updates the edited record
    const handleUpdate = () => {
        if (!validateInputs()) {
            alert('Invalid input values. Please correct the errors.');
            return;
        }

        // Updates meetings
        const updatedItem = { ...inputData };
        if (currentTab === 'Meetings') {
            const updatedMeetings = [...meetings];
            updatedMeetings[editIndex] = updatedItem;
            setMeetings(updatedMeetings);
        }
        // Updates calendars
        if (currentTab === 'Calendars') {
            const updatedCalendars = [...calendars];
            updatedCalendars[editIndex] = updatedItem;
            setCalendars(updatedCalendars);
        }
        // Participants
        if (currentTab === 'Participants') {
            const updatedParticipants = [...participants];
            updatedParticipants[editIndex] = updatedItem;
            setParticipants(updatedParticipants);
        }
        // Attachments
        if (currentTab === 'Attachments') {
            const updatedAttachments = [...attachments];
            updatedAttachments[editIndex] = updatedItem;
            setAttachments(updatedAttachments);
        }
        resetForm();  // Resets the form after updating
    };

    // Resets the form and editing state
    const resetForm = () => {
        setInputData({ uuid: '', title: '', details: '', dateTime: '', location: '', participantName: '', participantEmail: '', attachmentUrl: '', meetingId: '' });
        setIsEditing(false);
        setEditIndex(null);
    };

    return (
        <div className="container">
            {/* Tabs for the different records */}
            <div className="tabs">
                <button onClick={() => setCurrentTab('Meetings')}>Meetings</button>
                <button onClick={() => setCurrentTab('Calendars')}>Calendars</button>
                <button onClick={() => setCurrentTab('Participants')}>Participants</button>
                <button onClick={() => setCurrentTab('Attachments')}>Attachments</button>
            </div>

            <div className="content">
                <h2>{currentTab}</h2>
                <div className="form">
                    {/* Form inputs for each tab */}
                    {/* MEETINGS */}
                    {currentTab === 'Meetings' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Meeting UUID" />
                            <input name="title" value={inputData.title} onChange={handleInputChange} placeholder="Title (max 2000 characters)" />
                            <input name="dateTime" value={inputData.dateTime} onChange={handleInputChange} placeholder="Date and Time (YYYY-MM-DD HH:MM AM/PM)" />
                            <input name="location" value={inputData.location} onChange={handleInputChange} placeholder="Location (max 2000 characters)" />
                            <input name="details" value={inputData.details} onChange={handleInputChange} placeholder="Details (max 10000 characters)" />
                        </>
                    )}

                    {/* CALENDARS */}
                    {currentTab === 'Calendars' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Calendar UUID" />
                            <input name="title" value={inputData.title} onChange={handleInputChange} placeholder="Title (max 2000 characters)" />
                            <input name="details" value={inputData.details} onChange={handleInputChange} placeholder="Details (max 10000 characters)" />
                        </>
                    )}

                    {/* PARTICIPANTS */}
                    {currentTab === 'Participants' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Participant UUID" />
                            <input name="meetingId" value={inputData.meetingId} onChange={handleInputChange} placeholder="Meeting ID" />
                            <input name="participantName" value={inputData.participantName} onChange={handleInputChange} placeholder="Name (max 600 characters)" />
                            <input name="participantEmail" value={inputData.participantEmail} onChange={handleInputChange} placeholder="Email" />
                        </>
                    )}

                    {/* ATTACHMENTS */}
                    {currentTab === 'Attachments' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Attachment UUID" />
                            <input name="meetingId" value={inputData.meetingId} onChange={handleInputChange} placeholder="Meeting ID" />
                            <input name="attachmentUrl" value={inputData.attachmentUrl} onChange={handleInputChange} placeholder="Attachment URL" />
                        </>
                    )}

                    {/* CREATE or SAVE CHANGES button */}
                    <button onClick={isEditing ? handleUpdate : handleCreate}>
                        {isEditing ? 'Save Changes' : 'Create'}
                    </button>
                </div>

                {/* List of Items */}
                <div className="list">
                    {/* Displays the list for Meetings */}
                    {currentTab === 'Meetings' && meetings.map((meeting, index) => (
                        <div key={index} className="item">
                            <span>{meeting.uuid} - {meeting.title} - {meeting.dateTime} - {meeting.location} - {meeting.details}</span>
                            <button onClick={() => handleEdit(index)}>Update</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}

                    {/* Displays the list for Calendars */}
                    {currentTab === 'Calendars' && calendars.map((calendar, index) => (
                        <div key={index} className="item">
                            <span>{calendar.uuid} - {calendar.title} - {calendar.details}</span>
                            <button onClick={() => handleEdit(index)}>Update</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}

                    {/* Displays the list for Participants */}
                    {currentTab === 'Participants' && participants.map((participant, index) => (
                        <div key={index} className="item">
                            <span>{participant.uuid} - {participant.meetingId} - {participant.participantName} - {participant.participantEmail}</span>
                            <button onClick={() => handleEdit(index)}>Update</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}

                    {/* Displays the list for Attachments */}
                    {currentTab === 'Attachments' && attachments.map((attachment, index) => (
                        <div key={index} className="item">
                            <span>{attachment.uuid} - {attachment.meetingId} - {attachment.attachmentUrl}</span>
                            <button onClick={() => handleEdit(index)}>Update</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Home;
