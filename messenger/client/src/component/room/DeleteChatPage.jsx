import { useParams, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import axios from 'axios';
import { Box, Button, Typography } from '@mui/material';
import apiClient from "../../axios/apiClient.js";

function DeleteChatPage() {
    const { roomId } = useParams();
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleDelete = async () => {
        try {
            await apiClient().delete(`/delete_chat/${roomId}`);
            setMessage('Чат успешно удалён');
            setTimeout(() => navigate('/chats'), 2000); // Возврат на главную
        } catch (error) {
            setMessage('Ошибка при удалении чата');
        }
    };

    return (
        <Box p={4}>
            <Typography variant="h5">Удаление чата {roomId}</Typography>
            <Button variant="contained" color="error" onClick={handleDelete}>Удалить чат</Button>
            <Typography mt={2}>{message}</Typography>
        </Box>
    );
}

export default DeleteChatPage;
