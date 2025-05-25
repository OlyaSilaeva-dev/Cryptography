import React from 'react';
import { Button } from 'react-bootstrap';

const LogoutButton = () => {
    const handleLogout = () => {
        localStorage.clear();
        sessionStorage.clear();
        window.location.href = '/login';
    };

    return <Button variant="outline-danger" onClick={handleLogout}>Выйти</Button>;
};

export default LogoutButton;