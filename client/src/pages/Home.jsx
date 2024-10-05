import React, { useState } from 'react';
import { v4 as uuidv4 } from 'uuid';
import './Home.css';

const Home = () => {
    const [currentTab, setCurrentTab] = useState('Meetings');
    const [meetings, setMeetings] = useState([]);
    const [calendars, setCalendars] = useState([]);
    const [participants, setParticipants] = useState([]);
    const [attachments, setAttachments] = useState([]);

    const [inputData, setInputData] = useState({
        uuid: '',
        title: '',
        details: '',
        dateTime: '',
        location: '',
        participantName: '',
        participantEmail: '',
        attachmentUrl: '',
        meetingId: '',
        calendarIds: '',
        participantIds: '',
        attachmentIds: '',
        meetingIds: '',
    });

    const [isEditing, setIsEditing] = useState(false);
    const [editIndex, setEditIndex] = useState(null);

    const handleInputChange = (e) => {
        setInputData({ ...inputData, [e.target.name]: e.target.value });
    };

    // Validations
    const validateInputs = () => {
        // Ensures meeting validations are correct
        if (currentTab === 'Meetings') {
            // not sure how DATE/TIME works but it does
            return (
                inputData.title.length <= 2000 &&
                /^\d{4}-\d{2}-\d{2} \d{2}:\d{2} (AM|PM)$/.test(inputData.dateTime) &&
                inputData.location.length <= 2000 &&
                inputData.details.length <= 10000
            );
        }
        // Ensures calendar validations are correct
        if (currentTab === 'Calendars') {
            return inputData.title.length <= 2000 && inputData.details.length <= 10000;
        }
        // Ensures participant validations are correct
        if (currentTab === 'Participants') {
            return (
                inputData.participantName.length <= 600 &&
                /\S+@\S+\.\S+/.test(inputData.participantEmail)
            );
        }
        // Ensures attachments are URLs
        if (currentTab === 'Attachments') {
            return inputData.attachmentUrl.startsWith('http');
        }
        return true;
    };

    // Creates form if validations are met
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
        resetForm();
    };

    // Deletes on delete button
    const handleDelete = (index) => {
        if (currentTab === 'Meetings') setMeetings(meetings.filter((_, i) => i !== index));
        if (currentTab === 'Calendars') setCalendars(calendars.filter((_, i) => i !== index));
        if (currentTab === 'Participants') setParticipants(participants.filter((_, i) => i !== index));
        if (currentTab === 'Attachments') setAttachments(attachments.filter((_, i) => i !== index));
    };

    // Allows for edits
    const handleEdit = (index) => {
        setIsEditing(true);
        setEditIndex(index);
        if (currentTab === 'Meetings') setInputData(meetings[index]);
        if (currentTab === 'Calendars') setInputData(calendars[index]);
        if (currentTab === 'Participants') setInputData(participants[index]);
        if (currentTab === 'Attachments') setInputData(attachments[index]);
    };

    // Updates only if valid inputs
    const handleUpdate = () => {
        if (!validateInputs()) {
            alert('Invalid input values. Please correct the errors.');
            return;
        }

        // Props for the input data
        const updatedItem = { ...inputData };
        // Meetings
        if (currentTab === 'Meetings') {
            const updatedMeetings = [...meetings];
            updatedMeetings[editIndex] = updatedItem;
            setMeetings(updatedMeetings);
        }
        // Calendars
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
        resetForm();
    };

    // Resets the form on 'create' or 'save changes'
    const resetForm = () => {
        setInputData({
            uuid: '',
            title: '',
            details: '',
            dateTime: '',
            location: '',
            participantName: '',
            participantEmail: '',
            attachmentUrl: '',
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
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Meeting UUID" />
                            <input name="title" value={inputData.title} onChange={handleInputChange} placeholder="Title" />
                            <input name="dateTime" value={inputData.dateTime} onChange={handleInputChange} placeholder="Date and Time (YYYY-MM-DD HH:MM AM/PM)" />
                            <input name="location" value={inputData.location} onChange={handleInputChange} placeholder="Location" />
                            <textarea name="details" value={inputData.details} onChange={handleInputChange} placeholder="Details" />
                            <input name="calendarIds" value={inputData.calendarIds} onChange={handleInputChange} placeholder="Calendar IDs (comma-separated)" />
                            <input name="participantIds" value={inputData.participantIds} onChange={handleInputChange} placeholder="Participant IDs (comma-separated)" />
                            <input name="attachmentIds" value={inputData.attachmentIds} onChange={handleInputChange} placeholder="Attachment IDs (comma-separated)" />
                        </>
                    )}

                    {/* Calendar variables --> have to use textarea because of glitch for now */}
                    {currentTab === 'Calendars' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Calendar UUID" />
                            <input name="title" value={inputData.title} onChange={handleInputChange} placeholder="Calendar Title" />
                            <textarea name="details" value={inputData.details} onChange={handleInputChange} placeholder="Calendar Details" />
                            <input name="meetingIds" value={inputData.meetingIds} onChange={handleInputChange} placeholder="Meeting IDs (comma-separated)" />
                        </>
                    )}

                    {/* Participant variables */}
                    {currentTab === 'Participants' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Participant UUID" />
                            <input name="meetingId" value={inputData.meetingId} onChange={handleInputChange} placeholder="Meeting ID" />
                            <input name="participantName" value={inputData.participantName} onChange={handleInputChange} placeholder="Participant Name" />
                            <input name="participantEmail" value={inputData.participantEmail} onChange={handleInputChange} placeholder="Participant Email" />
                        </>
                    )}

                    {/* Attachment variables */}
                    {currentTab === 'Attachments' && (
                        <>
                            <input name="uuid" value={inputData.uuid} onChange={handleInputChange} placeholder="Attachment UUID" />
                            <input name="meetingId" value={inputData.meetingId} onChange={handleInputChange} placeholder="Meeting ID" />
                            <input name="attachmentUrl" value={inputData.attachmentUrl} onChange={handleInputChange} placeholder="Attachment URL" />
                        </>
                    )}

                    {/* Switches between update mode and create mode on selected tab */}
                    <button onClick={isEditing ? handleUpdate : handleCreate}>{isEditing ? 'Save Changes' : 'Create'}</button>
                </div>

                <div className="list">

                    {/* Meetings */}
                    {currentTab === 'Meetings' && meetings.map((meeting, index) => (
                        <div key={index} className="item">
                            <span>
                                <strong>UUID:</strong> {meeting.uuid} | <strong>Title:</strong> {meeting.title} | <strong>Date & Time:</strong> {meeting.dateTime} | <strong>Location:</strong> {meeting.location} | <strong>Details:</strong> {meeting.details} | <strong>Calendar IDs:</strong> {meeting.calendarIds} | <strong>Participant IDs:</strong> {meeting.participantIds} | <strong>Attachment IDs:</strong> {meeting.attachmentIds}
                            </span>
                            <button onClick={() => handleEdit(index)}>Edit</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}

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
                    {currentTab === 'Attachments' && attachments.map((attachment, index) => (
                        <div key={index} className="item">
                            <span>
                                <strong>UUID:</strong> {attachment.uuid} | <strong>Meeting ID:</strong> {attachment.meetingId} | <strong>URL:</strong> {attachment.attachmentUrl}
                            </span>
                            <button onClick={() => handleEdit(index)}>Edit</button>
                            <button onClick={() => handleDelete(index)}>Delete</button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Home;
