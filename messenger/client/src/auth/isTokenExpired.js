import { jwtDecode } from "jwt-decode"

const isTokenExpired = () => {
    try {
        let token = localStorage.getItem("token");
        if (token === null) return true;
        const decoded = jwtDecode(token);
        console.log(decoded.exp < Math.floor(Date.now / 1000));
        return decoded.exp < Math.floor(Date.now / 1000);
    } catch (error) {
        console.error('Invalid token:', error);
        return true;
    }
}

export default isTokenExpired;