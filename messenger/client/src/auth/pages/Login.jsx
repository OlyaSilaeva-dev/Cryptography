import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {Box, Button, Container, TextField} from "@mui/material";
import apiClient from "../../axios/apiClient.js";

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const handleLogin = async () => {
        event.preventDefault();

        try {
            if (!username || !password) {
                setError('Please enter both username and password.');
                return;
            }
            const response = await apiClient.post('/auth/login', {
                userName: username,
                password: password
            });

            setPassword("");
            localStorage.setItem("token", response.data.token);
            console.log('Login successful:', response.data);
            navigate("/chat");
        } catch (error) {
            console.error('Login failed:', error.response ? error.response.data : error.message);
            setError('Invalid username or password.');
        }
    }

    const inputStyles = {
        color: 'white',
        width: '300px',
        '& .MuiOutlinedInput-root': {
            borderRadius: '36px',
            '& fieldset': {
                borderColor: 'gray',
            },
            '& input': {
                height: '8px',
            },
        },
        '& .MuiOutlinedInput-notchedOutline': {
            borderColor: 'gray',
        },
    };

    return (
        <Container>
            <Box display="flex" flexDirection="column" justifyContent="center" alignItems="center" mt={2}>
                <h1>Вход</h1>
                <form onSubmit={handleLogin}>
                    <Box display="flex" flexDirection="column" alignItems="center" gap={2}>
                        <TextField
                            label="Username"
                            sx={inputStyles}
                            inputProps={{ style: { color: 'white' } }}
                            variant="outlined"
                            value={username}
                            InputLabelProps={{
                                style: { color: 'grey' }
                            }}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                        <TextField
                            sx={inputStyles}
                            inputProps={{ style: { color: 'white' } }}
                            label="Password"
                            type="password"
                            variant="outlined"
                            value={password}
                            InputLabelProps={{
                                style: { color: 'grey' }
                            }}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <Button
                            variant="contained"
                            sx={{
                                borderRadius: '36px',
                                width: '194px',
                                height: '42px',
                            }}
                            color="primary"
                            type="submit">
                            Вход
                        </Button>
                        <Button
                            variant="outlined"
                            sx={{
                                borderRadius: '36px',
                                width: '194px',
                                height: '42px',
                            }}
                            onClick={() => navigate('/registration')}
                        >
                            Регистрация
                        </Button>
                    </Box>
                </form>
            </Box>
        </Container>
    );
}

export default Login;