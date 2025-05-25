import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import isTokenExpired from "../auth/isTokenExpired";

const PrivateRoute = ({ children }) => {
    const location = useLocation();

    if (isTokenExpired()) {
        return <Navigate to="/login" replace state={{ from: location }} />;
    }

    return children;
};

export default PrivateRoute;
