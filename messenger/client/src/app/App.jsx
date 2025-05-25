import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';

import ChatPage from '../component/ChatPage.jsx';
import DeleteChatPage from '../component/room/DeleteChatPage.jsx';
import Login from "../auth/pages/Login.jsx";
import Registration from "../auth/pages/Registration.jsx";
import ChatRoom from "../component/room/ChatRoom.jsx";
import PrivateRoute from "../router/PrivateRoute.jsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Navigate to="/login" replace />} />
                <Route path="/login" element={<Login />} />
                <Route path="/registration" element={<Registration />} />

                {/* Защищённые маршруты */}
                <Route path="/chat" element={
                    <PrivateRoute>
                        <ChatPage />
                    </PrivateRoute>
                } />
                <Route path="/chat/:roomId" element={
                    <PrivateRoute>
                        <ChatRoom />
                    </PrivateRoute>
                } />
                <Route path="/chat/:roomId/delete" element={
                    <PrivateRoute>
                        <DeleteChatPage />
                    </PrivateRoute>
                } />

            </Routes>
        </BrowserRouter>
    );
}

export default App;
