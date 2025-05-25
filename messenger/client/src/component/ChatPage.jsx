import { useState, useEffect} from 'react';
import {Box, Typography, Paper, TextField, Autocomplete} from '@mui/material';
import ChatRoom from './room/ChatRoom.jsx';
import apiClient from "../axios/apiClient.js";

const MAX_LOCAL_USERS = 100;

function ChatPage() {
    const [user, setUser] = useState(null);
    const [selectedChat, setSelectedChat] = useState(null);
    const [chats, setChats] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [userOptions, setUserOptions] = useState([]);
    const [allUsersLoaded, setAllUsersLoaded] = useState(false);
    const [searchInput, setSearchInput] = useState('');

    const handleChatSelect = (chatId) => {
        setSelectedChat(chatId);
    };

    const handleChatCreate = async (selected) => {
        if (!selected || !user) return;

        try {
            const res = await apiClient.post('/chats/add', {
                firstUserId: user.id,
                secondUserId: selected.id,
            });

            setChats(prev => [...prev, res.data]);
            setSelectedUser(null);
            setSearchInput('');
        } catch (err) {
            console.error(err);
        }
    }

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const res = await apiClient.get('/user/info');
                setUser(res.data);
                console.log("User JSON от сервера:", res.data);
                localStorage.setItem("userId", res.data.id);
            } catch (err) {
                console.error(err);
            }
        };

        fetchUserInfo();
    }, []);

    useEffect(() => {
        const fetchChats = async () => {
            try {
                const res = await apiClient.get('/chats', {
                    params: { userId: localStorage.getItem("userId") },
                });
                setChats(res.data);
            } catch (err) {
                console.error(err);
            }
        };

        fetchChats();
    }, []);


    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const res = await apiClient.get('/user/all');
                if (res.data.length <= MAX_LOCAL_USERS) {
                    const filtered = res.data.filter(u => u.id !== user?.id);
                    setUserOptions(filtered);
                    setAllUsersLoaded(true);
                }
            } catch (err) {
                console.error(err);
            }
        };

        if (user) fetchUsers();
    }, [user]);

    useEffect(() => {
        const fetchSearchResults = async () => {
            if (allUsersLoaded || !searchInput) return;
            try {
                const res = await apiClient.get('/user/search', {
                    params: { query: searchInput }
                });
                const filtered = res.data.filter(u => u.id !== user?.id);
                setUserOptions(filtered);
            } catch (err) {
                console.error(err);
            }
        };

        const delayDebounce = setTimeout(fetchSearchResults, 300);
        return () => clearTimeout(delayDebounce);
    }, [searchInput, allUsersLoaded, user]);

    return (
        <Box sx={{ bgcolor: '#121212', height: '100vh', width: '100vw' }}>
            <Paper
                elevation={3}
                square
                sx={{
                    display: 'flex',
                    height: '100%',
                    width: '100%',
                    borderRadius: 0,
                    overflow: 'hidden',
                }}
            >
                {/* Левая колонка — список чатов */}
                <Box
                    width="260px"
                    bgcolor="#1e1e1e"
                    p={2}
                    sx={{ borderRight: '1px solid #333' }}
                >
                    <Typography variant="h6" gutterBottom sx={{ color: '#fff', mb: 2 }}>
                        Chats
                    </Typography>

                    <Autocomplete
                        options={userOptions}
                        getOptionLabel={(option) => option.username}
                        filterOptions={(options, state) =>
                            allUsersLoaded
                                ? options.filter(opt =>
                                    opt.username.toLowerCase().includes(state.inputValue.toLowerCase()))
                                : options
                        }
                        onChange={(e, value) => handleChatCreate(value)}
                        inputValue={searchInput}
                        onInputChange={(e, value) => setSearchInput(value)}
                        value={selectedUser}
                        renderInput={(params) => (
                            <TextField
                                {...params}
                                placeholder="Find a user..."
                                variant="outlined"
                                size="small"
                                sx={{
                                    input: { color: '#fff' },
                                    '& .MuiOutlinedInput-root': {
                                        '& fieldset': { borderColor: '#555' },
                                        '&:hover fieldset': { borderColor: '#888' },
                                        '&.Mui-focused fieldset': { borderColor: '#90caf9' },
                                    },
                                }}
                            />
                        )}
                    />

                    {chats
                        .filter(chat => chat?.chatRoom?.id)
                        .map((chat) => (
                        <Box
                            key={chat.chatRoom.id}
                            p={1.5}
                            mb={1}
                            onClick={() => handleChatSelect(chat.chatRoom.id)}
                            sx={{
                                backgroundColor: selectedChat === chat.chatRoom.id ? '#333' : 'transparent',
                                cursor: 'pointer',
                                borderRadius: 2,
                                color: selectedChat === chat.chatRoom.id ? '#90caf9' : '#ccc',
                                transition: '0.2s',
                                '&:hover': {
                                    backgroundColor: '#2c2c2c',
                                    color: '#90caf9',
                                },
                            }}
                        >
                            {chat.roomName}
                        </Box>
                    ))}
                </Box>

                {/* Правая колонка — выбранная комната */}
                <Box
                    flex={1}
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                    bgcolor="#181818"
                    sx={{ p: 2 }}
                >
                    {selectedChat ? (
                        <ChatRoom username={user?.username} roomId={selectedChat} />
                    ) : (
                        <Typography variant="h5" color="gray">
                            Choose who to write to...
                        </Typography>
                    )}
                </Box>
            </Paper>
        </Box>
    );
}

export default ChatPage;
