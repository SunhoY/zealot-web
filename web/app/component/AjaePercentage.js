import React, {Component} from 'react';

const styles = {
    container: {
        textAlign: "center",
        height: "140px",
        fontSize: "130px",
        fontFamily: "baemin",
        color: "#ffffff",
        marginTop: "60px"
    }
};

class AjaePercentage extends Component {
    constructor(props) {
        super(props);

        let percentage = props.percentage;

        if(percentage >= 0 && percentage < 70) {
            this.ajaeMessageColor = "not_ajae_color";
        } else if(percentage >= 70 && percentage < 90) {
            this.ajaeMessageColor = "medium_ajae_color";
        } else {
            this.ajaeMessageColor = "full_ajae_color";
        }
    }

    render() {
        return (
            <div style={styles.container}>
                <div>{this.props.nickName} 님의 아재력</div>
                <div style={{marginTop: "60px"}} className={this.ajaeMessageColor}>{this.props.percentage} %</div>
            </div>
        );
    }
}

export default AjaePercentage;