import {useEffect, useRef, useState} from 'react';
import {Client} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {Box, Button, TextField} from "@mui/material";
import ChatMessage from "../message/ChatMessage.jsx";

function ChatRoom({username, roomId}) {
    const [messages, setMessages] = useState([]);
    const [client, setClient] = useState(null);
    const [keyExchangeDone, setKeyExchangeDone] = useState(false);
    const [connectionStatus, setConnectionStatus] = useState("Connecting...");
    const messageInputRef = useRef();
    const messagesEndRef = useRef();
    const keyExchangeDoneRef = useRef(false);
    const [sharedSecrets, setSharedSecrets] = useState({});
    const sessionDataRef = useRef({}); // { [roomId]: { privateKey, p, g, peerPublicKey } }

    useEffect(() => {
        const stompClient = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws?token=' + localStorage.getItem('token')),
            reconnectDelay: 5000,
            onConnect: () => {
                setConnectionStatus("Connected");

                stompClient.subscribe(`/topic/${roomId}`, message => {
                    const newMsg = JSON.parse(message.body);

                    setMessages(prev => {
                        const exists = prev.some(m => m.id === newMsg.id && m.sessionId === newMsg.sessionId);
                        return exists ? prev : [...prev, newMsg];
                    });
                });


                if (!keyExchangeDoneRef.current) {
                    stompClient.subscribe("/user/queue/key-exchange", message => {
                        console.log("Key exchange response:", message.body);
                        const { publicKey } = JSON.parse(message.body);

                        stompClient.publish({
                            destination: '/app/chat.public-key',
                            body: JSON.stringify({ publicKey })
                        });

                        keyExchangeDoneRef.current = true;
                        setKeyExchangeDone(true);
                    });

                    stompClient.publish({
                        destination: "/app/chat.key-exchange",
                        body: ""
                    });
                }

                setClient(stompClient);
            }
        });

        stompClient.activate();

        return () => {
            if (stompClient.connected) stompClient.deactivate();
        };
    }, [roomId]);

    const sendMessage = () => {
        const content = messageInputRef.current.value;
        if (!content || !client || !client.connected || !keyExchangeDone) return;

        client.publish({
            destination: "/app/chat.send-message",
            body: JSON.stringify({
                sender: username,
                content,
                type: "CHAT",
                roomId
            })
        });

        // Не добавляем сообщение вручную! Ждём, пока придёт с сервера
        messageInputRef.current.value = '';
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter' && !event.shiftKey) {
            sendMessage();
            event.preventDefault();
        }
    };

    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({behavior: 'smooth'});
    }, [messages]);

    return (
        <Box display="flex" color={"darkGray"} flexDirection="column" justifyContent="center" alignItems="center"
             mt={2}>
            <h2>{connectionStatus}</h2>
            <Box sx={{height: '500px', overflow: 'auto', width: '100%'}}>
                {messages.map((msg, i) => (
                    <ChatMessage key={i} message={msg} username={username}/>
                ))}
                <div ref={messagesEndRef}/>
            </Box>
            <Box display="flex" justifyContent="center" alignItems="stretch" mt={2}>
                <TextField
                    sx={{
                        color: 'white', '& .MuiOutlinedInput-notchedOutline': {borderColor: 'gray'},
                        width: '300px',
                        height: '10px',
                        '& .MuiOutlinedInput-root': {
                            borderRadius: '36px',
                            '& fieldset': {
                                borderColor: 'gray',
                            },
                            '& input': {
                                height: '10px',
                            },
                        },
                    }}
                    inputProps={{style: {color: 'white'}}}
                    inputRef={messageInputRef}
                    variant="outlined"
                    placeholder="Type a message..."
                    onKeyDown={handleKeyDown}
                />

                <Box marginLeft={2}>
                    <Button
                        variant="contained"
                        color="primary"
                        sx={{
                            width: '94px',
                            height: '42px',
                            borderRadius: '36px',
                        }}
                        onClick={sendMessage}>
                        Send
                    </Button>
                </Box>
            </Box>
        </Box>
    );
}

export default ChatRoom;
