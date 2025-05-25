import {useState} from 'react';
import {Button, TextField, Container, Box} from '@mui/material';
import apiClient from '../../axios/apiClient.js';
import { useNavigate } from 'react-router-dom';

function Registration() {

    const [inputUsername, setInputUsername] = useState('');
    const [inputPassword, setInputPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const history = useNavigate();
    const [error, setError] = useState(null);

    const handleRegistrationSubmit = async (event) => {
        event.preventDefault();

        try {
            if (!inputUsername || !inputPassword || !confirmPassword) {
                setError('Please fill in all fields');
                return;
            }

            if (inputPassword !== confirmPassword) {
                throw new Error("Passwords do not match");
            }

            const response = await apiClient.post('/auth/register', {
                userName: inputUsername,
                password: inputPassword
            });

            console.log(response.data);
            setConfirmPassword("");
            setInputPassword("");
            localStorage.setItem("token", response.data.token);
            console.log('Register successful:', response.data);
            window.location.href = "/login";
        } catch (err) {
            console.error(err);
            setError('Registration failed');
        }
    };

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
                <h1>Регистрация</h1>
                <form onSubmit={handleRegistrationSubmit}>
                    <Box display="flex" flexDirection="column" alignItems="center" gap={2}>
                        <TextField
                            label="Username"
                            sx={inputStyles}
                            inputProps={{ style: { color: 'white' } }}
                            variant="outlined"
                            value={inputUsername}
                            InputLabelProps={{
                                style: { color: 'grey' }
                            }}
                            onChange={(e) => setInputUsername(e.target.value)}
                        />
                        <TextField
                            sx={inputStyles}
                            inputProps={{ style: { color: 'white' } }}
                            label="Password"
                            type="password"
                            variant="outlined"
                            value={inputPassword}
                            InputLabelProps={{
                                style: { color: 'grey' }
                            }}
                            onChange={(e) => setInputPassword(e.target.value)}
                        />
                        <TextField
                            sx={inputStyles}
                            inputProps={{ style: { color: 'white' } }}
                            label="Repeat the password"
                            type="password"
                            variant="outlined"
                            value={confirmPassword}
                            InputLabelProps={{
                                style: { color: 'grey' }
                            }}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                        />
                        <Button
                            variant="contained"
                            sx={{
                                width: '194px',
                                height: '42px',
                                borderRadius: '36px',
                            }}
                            color="primary"
                            type="submit">
                            Регистрация
                        </Button>
                    </Box>
                </form>
            </Box>
        </Container>
    );
}

export default Registration;